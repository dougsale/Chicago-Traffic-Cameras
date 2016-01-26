/*
 * Cameras client uses the Google Maps API to generate routes,
 * and display them on a map along with any Chicago-municipality
 * traffic cameras capable of automatically generating traffic
 * tickets.
 * 
 * Cameras client transforms the route information from Google
 * (com.google.maps.DirectionsResult) into a Route understood
 * by the cameras service.
 * 
 * Example route:
 * {
 * 	"startAddress":"201 N Columbus Dr, Chicago, IL 60611, USA",
 * 	"endAddress":"400 E Illinois St, Chicago, IL 60611, USA",
 * 	"steps":[
 * 		{
 * 			"instructions":"Head <b>south</b> on <b>N Columbus Dr</b> toward <b>E Lower Wacker Dr</b>",
 * 			"start":{"latitude":41.8880992,"longitude":-87.6207005},
 * 			"end":{"latitude":41.88776319999999,"longitude":-87.62070829999999}
 * 		},
 * 		{
 * 			"instructions":"Make a <b>U-turn</b> at <b>E Lower Wacker Dr</b>",
 * 			"start":{"latitude":41.88776319999999,"longitude":-87.62070829999999},
 * 			"end":{"latitude":41.8910117,"longitude":-87.62012340000001}
 * 		},
 * 		{
 * 			"instructions":"Turn <b>right</b> onto <b>E Illinois St</b><div style=\"font-size:0.9em\">Destination will be on the left</div>",
 * 			"start":{"latitude":41.8910117,"longitude":-87.62012340000001},
 * 			"end":{"latitude":41.8910524,"longitude":-87.6178458}
 * 		}
 * 	]
 * }
 * 
 * Cameras service returns camera data as JSON, which is then 
 * displayed as markers on the map.
 * 
 * Example cameras:
 * 	{
 * 		"redLightCameras":[
 * 			{
 * 				"latitude":41.891002,
 * 				"longitude":-87.620224,
 * 				"intersection":["Columbus","Illinois"],
 * 				"approaches":["NORTHBOUND","SOUTHBOUND"]
 * 			}
 * 		],
 * 		"speedCameras":[
 * 			{
 * 				"latitude":41.89003775504898,
 * 				"longitude":-87.62017903427099,
 * 				"address":"449 N Columbus Dr",
 * 				"approaches":["NORTHBOUND"]
 * 			},
 * 			{
 * 				"latitude":41.890122352505166,
 * 				"longitude":-87.62041639513696,
 * 				"address":"450 N Columbus Dr",
 * 				"approaches":["SOUTHBOUND"]
 * 			},
 * 			{
 * 				"latitude":41.89091009572781,
 * 				"longitude":-87.61934752145963,
 * 				"address":"319 E Illinois St",
 * 				"approaches":["EASTBOUND"]
 * 			}
 * 		]
 * 	}
 * 
 * Cameras client requires Google Maps API and JQuery.
 */
var camerasClient = (function (jq) {

	// PRIVATE FIELDS //
	
	var serviceUrl = "cameras";	
	var cameraMarkers = [];
	var speedCameraMarkerIcon = "images/glyphicons-332-dashboard.png";
	var redLightCameraMarkerIcon = "images/glyphicons-533-stop-sign.png";

	// DOM Elements
	var
		mapContainer = null,
		startAddressInput = null,
		endAddressInput = null,
		mapButton = null;
	
	// Google domain objects
	var
		map = null,
		directionsService = null,
		directionsRenderer = null; 

	
	// PRIVATE FUNCTIONS // 
	
	function initDOM(mapId, startAddressId, endAddressId, mapButtonId) {
		//TODO validate input
		// get handles to DOM elements
		mapContainer = jq("#" + mapId)[0];
		startAddressInput = jq("#" + startAddressId)[0];
		endAddressInput = jq("#" + endAddressId)[0];
		mapButton = jq("#" + mapButtonId)[0];
		
		mapButton.addEventListener("click", mapRoute);
	}
	
	function initGoogle() {

		// size the map container
		var useragent = window.navigator.userAgent;
		if (useragent.indexOf('iPhone') != -1 || useragent.indexOf('Android') != -1 ) {
			mapContainer.style.width = '100%';
			mapContainer.style.height = '100%';
		} else {
		    mapContainer.style.width = '800px';
		    mapContainer.style.height = '600px';
		}
		
		// create the map
		map = new google.maps.Map(
			mapContainer,
			{
				center: { lat: 41.850, lng: -87.650 }, // ~= Chicago
				zoom: 11
			}
		);
		
		directionsService = new google.maps.DirectionsService;
		directionsRenderer = new google.maps.DirectionsRenderer({map: map});
	}
	
	function mapRoute() {
		//TODO validate input
		var startAddress = startAddressInput.value;
		var endAddress = endAddressInput.value;
		
		directionsService.route(
			{
				origin: startAddress,
				destination: endAddress,
				travelMode: google.maps.TravelMode.DRIVING
			},
			function (directionsResult, directionsStatus) {
				if (directionsStatus === google.maps.DirectionsStatus.OK) {
					mapCameras(directionsResult);
				} else {
					handleError("Directions request failed", directionsStatus);
				}
			}
		);
	}
	
	function mapCameras(directionsResult) {
		var route = routeForDirectionsResult(directionsResult);
		//TODO impl clean debug method
		//console.log(JSON.stringify(route));
		var jqXHR = jq.getJSON(serviceUrl, "route=" + JSON.stringify(route));
		
		jqXHR.done(function (cameras, textStatus, jqXHR) {
			//console.log(JSON.stringify(cameras));
			updateMap(directionsResult, cameras);
		});
		
		jqXHR.fail(function (jqXHR, textStatus, errorThrown) {
			handleError(textStatus, errorThrown);
		});
	}
	
	function updateMap(directionsResult, cameras) {
		
		// clear existing camera markers from map
		// markers are reused over all requests
		for (var x = 0; x < cameraMarkers.length; x++) {
			cameraMarkers[x].setMap(null);
		}

		var speedCameras = cameras.speedCameras;
		var speedCameraCount = speedCameras.length;
		for (var i = 0; i < speedCameraCount; i++) {
			updateSpeedCameraMarker(speedCameras[i], getMarker(i));
		}
		
		var redLightCameras = cameras.redLightCameras;
		var redLightCameraCount = redLightCameras.length;
		for (var j = 0; j < redLightCameraCount; j++) {
			updateRedLightCameraMarker(redLightCameras[j], getMarker(i + j));
		}

		directionsRenderer.setDirections(directionsResult);
	}
	
	/*
	 * Return an existing marker or a new one, when needed.
	 * Store markers for reuse.
	 */
	function getMarker(index) {
		return cameraMarkers[index] = cameraMarkers[index] || new google.maps.Marker;
	}
	
	function updateSpeedCameraMarker(camera, marker) {
		var title = camera["address"];
		title += ": ";
		
		var approaches = camera["approaches"];
		for (var i = 0; i < approaches.length; i++) {
			title += approaches[i].toLowerCase();
			if (i + 1 < approaches.length)
				title += ", ";
		}

		marker.setTitle(title);
		marker.setPosition({ lat: camera["latitude"], lng: camera["longitude"] });
		marker.setIcon(speedCameraMarkerIcon);
		marker.setMap(map);
	}

	function updateRedLightCameraMarker(camera, marker) {
		var title = "";
		
		var intersection = camera["intersection"];
		for (var i = 0; i < intersection.length; i++) {
			title += intersection[i];
			if (i + 1 < intersection.length)
				title += " & ";
		}

		title += ": ";
		
		var approaches = camera["approaches"];
		for (var j = 0; j < approaches.length; j++) {
			title += approaches[j].toLowerCase();
			if (j + 1 < approaches.length)
				title += ", ";
		}

		marker.setTitle(title);
		marker.setPosition({ lat: camera["latitude"], lng: camera["longitude"] });
		marker.setIcon(redLightCameraMarkerIcon);
		marker.setMap(map);
	}

	/*
	 * Transform Google DirectionsResult into Route
	 */
	function routeForDirectionsResult(directionsResult) {

		var directionsLeg = directionsResult.routes[0].legs[0];
		var startAddress = directionsLeg.start_address;
		var endAddress = directionsLeg.end_address;
		
		var directionsSteps = directionsLeg.steps;
		var stepsCount = directionsSteps.length;
		
		var steps = [stepsCount];
		
		var directionsStep;
		for (var i = 0; i < stepsCount; i++) {
			var directionsStep = directionsSteps[i];
			steps[i] = {
				"instructions" : directionsStep.instructions,
				"start" : {
					"latitude" : directionsStep.start_location.lat(),
					"longitude" : directionsStep.start_location.lng()
				},
				"end" : {
					"latitude" : directionsStep.end_location.lat(),
					"longitude" : directionsStep.end_location.lng()
				}			
			}
		}
		
		var route = {
			"startAddress" : startAddress,
			"endAddress" : endAddress,
			"steps" : steps
		};
			
		return route;
	}
	
	function handleError(message, error) {
		//TODO error handling
		window.alert(message + ": " + error);
		console.warn(message + ": " + error);
	}
	
	// PUBLIC INTERFACE //
	
	return {
		
		init: function(mapId, startAddressId, endAddressId, mapButtonId) {
			initDOM(mapId, startAddressId, endAddressId, mapButtonId);
			initGoogle();
		}
		
	};
	
})($);
