function _ServiceClient() {
    return {
        port: parseInt("[port]"),
        host: "[host]",
        getBaseUrl: function() {
            return "http://" + this.host + ":" + this.port;
        },
        get_EndpointCall: function(input) {
            return this.createCall("_Endpoint", input);
        },
        createCall: function(activityName, payload) {
            response = makeRequest("POST", this.getBaseUrl() + "/activity?activity=" + activityName + "&encoding=JSON&payload=" + encodeURIComponent(JSON.stringify(payload)), true);
            return response;
        }
    }
}


var makeRequest = function (method, url) {

	// Create the XHR request
	var request = new XMLHttpRequest();

	// Return it as a Promise
	return new Promise(function (resolve, reject) {

		// Setup our listener to process compeleted requests
		request.onreadystatechange = function () {

			// Only run if the request is complete
			if (request.readyState !== 4) return;

			// Process the response
			if (request.status >= 200 && request.status < 300) {
				// If successful
				resolve(request);
			} else {
				// If failed
				reject({
					status: request.status,
					statusText: request.statusText
				});
			}

		};

		// Setup our HTTP request
		request.open(method || 'GET', url, true);

		// Send the request
		request.send();

	});
};
