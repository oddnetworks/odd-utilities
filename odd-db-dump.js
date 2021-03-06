// Odd DB Dump
// A simple script to dump the collection and video data from an existing
// Oddworks server instance. Includes virtually no safety net, error checks
// or methods of recovery. Use at your own risk. Having said that the worst
// it can probably do is write a bunch of json to your hard drive
//
// v1.0.0 pbm 4/21/16

'use strict';

const https = require('https');
const fs = require('fs');

const rootPath = __dirname;

function verifyDirExists(dir) {
	if (!fs.existsSync(dir)) {
		fs.mkdirSync(dir);
		return fs.existsSync(dir);
	}
}

function write(data, path) {
	try {
		// console.log(data);
		console.log(`Writing: ${path}`);
		fs.writeFile(path, data, function (e) {
			if (e) {
				return console.log(e);
			}
			console.log(`JSON written to file: ${path}`);
		});
	} catch (e) {
		console.error(e);
	} finally {

	}
}

function writeDataToFile(data) {
	const type = data.type;
	const id = data.id;
	if (typeof type === 'undefined' || typeof id === 'undefined') {
		console.error('Unable to determine object type or id. Writing data aborted');	
	} else {
		const pathToWrite = `${rootPath}/data/${type}/${id}.json`;
		write(JSON.stringify(data), pathToWrite);
	}
}

function parseData(data) {
	var json = JSON.parse(data);
	var elements = json.data;
	if (elements.length > 0) {
		var firstElement = elements[0]
		if (verifyDirExists(`${rootPath}/data/${firstElement.type}`)) {
			elements.forEach(function (element) {
				// console.log(element);
				writeDataToFile(element);
			}, this);		
		} else {
			console.error('Unable to create required directories');
		}
	}
};

function fetchData(type) {
	var headers = {
		'User-Agent': 'tvOS',
		'Content-Type': 'application/json',
		'Accept': 'application/json',
		'x-access-token': '<the access token requried for your server>'
	}

	// the host name should be the root to your server
	// path should be the base path for the route to query for objects
	// adjust accordingly
	var options = {
		hostname: 'beta.oddworks.io',
		port: 443,
		path: `/v2/${type}`,
		method: 'GET',
		headers: headers
	};

	https.get(options, (response) => {
		if (response.statusCode === 200) {
			var theJSON = '';

			response.on('data', function (chunk) {
				theJSON += chunk;
			});

			response.on('end', function () {
				console.log('writing to file...');
				parseData(theJSON);
			});
		} else {
			console.log(response.statusCode);
		}

		response.on('error', (error) => {
			console.error('Error fetching: ' + error.message);
		});
	});
}

// start by creating a root data folder in the current directory
// then write all the video and collection data with the data folder
// should work for other assets as well. not tested beyond video/collection
fs.mkdirSync(`${rootPath}/data/`);
fetchData('videos');
fetchData('collections');
