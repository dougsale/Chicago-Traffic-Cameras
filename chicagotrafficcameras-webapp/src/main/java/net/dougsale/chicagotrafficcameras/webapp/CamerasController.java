package net.dougsale.chicagotrafficcameras.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.dougsale.chicagotrafficcameras.domain.Cameras;
import net.dougsale.chicagotrafficcameras.domain.Route;
import net.dougsale.chicagotrafficcameras.repository.RepositoryException;
import net.dougsale.chicagotrafficcameras.services.CamerasService;
import net.dougsale.chicagotrafficcameras.webapp.mappers.CamerasToJsonMapper;
import net.dougsale.chicagotrafficcameras.webapp.mappers.MapperException;
import net.dougsale.chicagotrafficcameras.webapp.mappers.RouteFromJsonMapper;
import static org.apache.commons.lang3.Validate.*;

@Controller
public class CamerasController {

	private CamerasToJsonMapper camerasMapper;
	private RouteFromJsonMapper routeMapper;
	private CamerasService cameraService;
	
	public CamerasController(CamerasService camerasService, RouteFromJsonMapper routeMapper, CamerasToJsonMapper camerasMapper) {
		notNull(camerasService);
		notNull(routeMapper);
		notNull(camerasMapper);
		this.routeMapper = routeMapper;
		this.cameraService = camerasService;
		this.camerasMapper = camerasMapper;
	}

	public CamerasToJsonMapper getCamerasToJsonMapper() {
		return camerasMapper;
	}

	public RouteFromJsonMapper getRouteFromJsonMapper() {
		return routeMapper;
	}

	public CamerasService getCamerasService() {
		return cameraService;
	}
	
	@RequestMapping(path="/cameras", method=RequestMethod.GET, params="!route")
	public @ResponseBody String getAllCameras() throws RepositoryException, MapperException {
		Cameras cameras = cameraService.getCameras();
		String camerasJson = camerasMapper.map(cameras);
		return camerasJson;
	}
	
	@RequestMapping(path="/cameras", method=RequestMethod.GET, params="route")
	public @ResponseBody String getCameras(@RequestParam("route") String routeJson) throws RepositoryException, MapperException {
		Route route = routeMapper.map(routeJson);
		Cameras cameras = cameraService.getCameras(route);
		String camerasJson = camerasMapper.map(cameras);
		return camerasJson;
	}
	
}
