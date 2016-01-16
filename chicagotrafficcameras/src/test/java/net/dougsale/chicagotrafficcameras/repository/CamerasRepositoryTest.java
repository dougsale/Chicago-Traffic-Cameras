/**
 * Copyright (c) 2015 Doug Sale
 * All rights reserved.
 */
package net.dougsale.chicagotrafficcameras.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import org.junit.Test;

import net.dougsale.chicagotrafficcameras.domain.Cameras;

public class CamerasRepositoryTest {

	//
	// test constructor:
	//
	
	@Test
	public void testCamerasRepository() {
		String resourceName = "resourceName";
		CamerasRepository repo = new CamerasRepository(resourceName);
		assertThat(repo.getResourceName(), equalTo(resourceName));
	}

	@Test(expected=NullPointerException.class)
	public void testCamerasRepository_NullResource() {
		new CamerasRepository(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCamerasRepository_EmptyResource() {
		new CamerasRepository("  ");
	}

	// test business method
	
	@Test
	public void testGetCameras() throws IOException, RepositoryException {
		Cameras repoCameras = new Cameras();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		new ObjectOutputStream(bytes).writeObject(repoCameras);
		ByteArrayInputStream in = new ByteArrayInputStream(bytes.toByteArray());
		
		// partial mock, not feeling too bad about it.  buck has to stop somewhere.
		//TODO maybe java 8 style, i could provide a functor to grab resource and avoid partial
		// mock smell...
		CamerasRepository repo = new CamerasRepository("foo.ser");
		CamerasRepository spy = spy(repo);
		doReturn(in).when(spy).getResourceAsStream();
		
		Cameras cameras = spy.getCameras();
		assertThat(cameras, equalTo(repoCameras));
	}
	
	//
	// test helper methods:
	//
	
	// readCameras(ObjectInput):

	@Test
	public void testReadCameras() throws IOException, ClassNotFoundException, RepositoryException {
		Cameras cameras = mock(Cameras.class);		
		ObjectInput in = when(mock(ObjectInput.class).readObject()).thenReturn(cameras).getMock();
		Cameras result = new CamerasRepository("foo.ser").readCameras(in);
		verify(in, times(1)).readObject();
		assertThat(result, sameInstance(cameras));
	}
	
	@Test
	public void testReadCameras_InvalidFormat() throws IOException, ClassNotFoundException {
		Exception[] causes = {
				mock(InvalidClassException.class),
				mock(StreamCorruptedException.class),
				mock(OptionalDataException.class)
		};

		for (Exception cause : causes) {
			ObjectInput in = when(mock(ObjectInput.class).readObject()).thenThrow(cause).getMock();
			
			try {
				new CamerasRepository("foo.ser").readCameras(in);
				fail();
			} catch (RepositoryException e) {
				assertThat(e.getCause(), equalTo(cause));
				assertThat(e.getErrCode(), equalTo(RepositoryCode.INVALID_FORMAT));
				assertThat(e.getContextData().get("resourceName"), equalTo("foo.ser"));
			}
		}
	}
	
	@Test
	public void testReadCameras_ReadFailed() throws IOException, ClassNotFoundException {
		Exception cause = mock(IOException.class);
		ObjectInput in = when(mock(ObjectInput.class).readObject()).thenThrow(cause).getMock();
		
		try {
			new CamerasRepository("foo.ser").readCameras(in);
			fail();
		} catch (RepositoryException e) {
			assertThat(e.getCause(), equalTo(cause));
			assertThat(e.getErrCode(), equalTo(RepositoryCode.READ_FAILED));
			assertThat(e.getContextData().get("resourceName"), equalTo("foo.ser"));
		}
	}
	
	@Test
	public void testReadCameras_MissingClass() throws IOException, ClassNotFoundException {
		Exception cause = mock(ClassNotFoundException.class);
		ObjectInput in = when(mock(ObjectInput.class).readObject()).thenThrow(cause).getMock();
		
		try {
			new CamerasRepository("foo.ser").readCameras(in);
			fail();
		} catch (RepositoryException e) {
			assertThat(e.getCause(), equalTo(cause));
			assertThat(e.getErrCode(), equalTo(RepositoryCode.MISSING_CLASS));
			assertThat(e.getContextData().get("resourceName"), equalTo("foo.ser"));
		}
	}
	
	// close(InputStream)
	
	@Test
	public void testClose() throws RepositoryException, IOException {
		InputStream in = mock(InputStream.class);
		new CamerasRepository("foo.ser").close(in);
		verify(in, times(1)).close();
	}
	
	@Test
	public void testClose_CloseFailed() throws IOException {
		Exception cause = mock(IOException.class);
		InputStream in = mock(InputStream.class);
		doThrow(cause).when(in).close();
		
		try {
			new CamerasRepository("foo.ser").close(in);
			fail();
		} catch (RepositoryException e) {
			assertThat(e.getCause(), equalTo(cause));
			assertThat(e.getErrCode(), equalTo(RepositoryCode.CLOSE_FAILED));
			assertThat(e.getContextData().get("resourceName"), equalTo("foo.ser"));
		}
	}

	// getResourceAsStream()
	
	@Test
	public void testGetResourceAsStream() throws RepositoryException {
		// tests that something we know is there is loaded successfully
		String resourceName = CamerasRepository.class.getCanonicalName().replace('.', '/') + ".class";

		InputStream in = new CamerasRepository(resourceName).getResourceAsStream();
		assertThat(in, not(nullValue()));
	}
	
	@Test
	public void testGetResourceAsStream_Unavailable() {
		// tests that something we know is not there throws the correct exception
		String resourceName = "UnavailableResource.ser";

		try {
			new CamerasRepository(resourceName).getResourceAsStream();
			fail();
		} catch (RepositoryException e) {
			assertThat(e.getCause(), nullValue());
			assertThat(e.getErrCode(), equalTo(RepositoryCode.UNAVAILABLE));
			assertThat(e.getContextData().get("resourceName"), equalTo(resourceName));
		}
	}
	
	// getObjectInputStream(InputStream)
	
	@Test
	public void testGetObjectInputStream() throws RepositoryException, IOException {
		// tests creating an objectinputstream from a valid input stream
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		new ObjectOutputStream(bytes).writeObject(new Exception()); // used exception cuz it's serializable
		ByteArrayInputStream in = new ByteArrayInputStream(bytes.toByteArray());
		
		ObjectInputStream stream = new CamerasRepository("foo.ser").getObjectInputStream(in);
		assertThat(stream, not(nullValue()));
	}
	
	@Test
	public void testGetObjectInputStream_InvalidFormat() throws IOException {
		// tests that invalid header info is handled properly
		// on instantiation, objectinputstream tries to read some header info from the underlying input stream
		Exception cause = mock(StreamCorruptedException.class);
		InputStream in = when(mock(InputStream.class).read(any(), anyInt(), anyInt())).thenThrow(cause).getMock();
		String resourceName = "foo.ser";

		try {
			new CamerasRepository(resourceName).getObjectInputStream(in);
			fail();
		} catch (RepositoryException e) {
			assertThat(e.getCause(), equalTo(cause));
			assertThat(e.getErrCode(), equalTo(RepositoryCode.INVALID_FORMAT));
			assertThat(e.getContextData().get("resourceName"), equalTo(resourceName));
		}
	}
	
	@Test
	public void testGetObjectInputStream_ReadFailed() throws IOException {
		// tests that an io error is handled properly
		// on instantiation, objectinputstream tries to read some header info from the underlying input stream
		Exception cause = mock(IOException.class);
		InputStream in = when(mock(InputStream.class).read(any(), anyInt(), anyInt())).thenThrow(cause).getMock();
		String resourceName = "foo.ser";

		try {
			new CamerasRepository(resourceName).getObjectInputStream(in);
			fail();
		} catch (RepositoryException e) {
			assertThat(e.getCause(), equalTo(cause));
			assertThat(e.getErrCode(), equalTo(RepositoryCode.READ_FAILED));
			assertThat(e.getContextData().get("resourceName"), equalTo(resourceName));
		}
	}

	//	@Test
//	public void testGetCamerasResourceReadFailed() throws IOException {
//		IOException cause = mock(IOException.class);
//		InputStream mockStream = when(mock(InputStream.class).read(any(), anyInt(), anyInt())).thenThrow(cause).getMock();
//		
//		try {
//			new CamerasRepository("Foo.ser").getCameras(mockStream);
//			fail();
//		} catch (RepositoryException e) {
//			assertThat(e.getErrCode(), equalTo(RepositoryCode.READ_FAILED));
//			assertThat(e.getContextData().get("resourceName"), equalTo("Foo.ser"));
//			assertThat(e.getCause(), equalTo(cause));
//		}
//	}
//	
//	
//	@Test
//	public void testGetCamerasResourceCloseFailed() throws IOException {
//		IOException cause = mock(IOException.class);
//		InputStream mockStream = mock(InputStream.class);
//		doThrow(cause).when(mockStream).close();
//		
//		try {
//			new CamerasRepository("Foo.ser").getCameras(mockStream);
//			fail();
//		} catch (RepositoryException e) {
//			assertThat(e.getErrCode(), equalTo(RepositoryCode.READ_FAILED));
//			assertThat(e.getContextData().get("resourceName"), equalTo("Foo.ser"));
//			assertThat(e.getCause(), equalTo(cause));
//		}
//	}

}
