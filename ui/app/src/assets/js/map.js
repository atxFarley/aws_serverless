var leafletMap = (function () {
  'use strict'

  let map;
  let fieldsLayer;
  let basemaps = {};
  let overlays = {};
  let featureSelection;
  let featureLayer;
  let layerControl;
  let apiURL = "";
  let fieldsURLPath = "/fields";
  let searchFieldsURLPath = "/fields/search";
  let searchFieldsURLQueryParamName = "search";
  let editableLayers;
  let recentSearchBoxValue = "";
  let mapBoxURL = "";

  function init(apiUrl, mapboxurl) {
    console.log("in leafletMap init(apiUrl: " + apiUrl + ", mapboxurl: " + mapboxurl + ")");

    // create map and set center and zoom level
    try {
      apiURL = apiUrl;
      mapBoxURL = mapboxurl;
      console.log("apiURL: " + apiURL + ", mpaBoxURL: " + mapBoxURL);
      map = new L.map('mapid');
      map.setView([32.112355, -98.537303], 12);
      map.options.minZoom = 5;
      // map.options.maxZoom = 12;


      //https://leaflet.github.io/Leaflet.draw/docs/leaflet-draw-latest.html

      //Initialise the FeatureGroup to store editable layers
      editableLayers = new L.FeatureGroup();
      editableLayers.addTo(map);

      const homeButton = L.easyButton('fa fa-home', function (btn, map) {
        try {
          map.fitBounds(editableLayers.getBounds());
        } catch (e) {
          console.error("cannot set map to layer bounds, setting to initial view");
          map.setView([32.112355, -98.537303], 12);
        }
      });

      map.addControl(homeButton);

      const search = new GeoSearch.GeoSearchControl({
        provider: new GeoSearch.OpenStreetMapProvider(),
        searchLabel: 'Enter address',
      });
      map.addControl(search);


      var drawPluginOptions = {
        position: 'bottomleft',
        draw: {
          marker: true,
          circlemarker: false,
          polyline: false,
          // {
          //       shapeOptions: {
          //         color: '#f357a1',
          //         weight: 10
          //       }
          //     },
          polygon: {
            allowIntersection: false, // Restricts shapes to simple polygons
            showLength: true,
            showArea: true,
            metric: false,
            drawError: {
              color: '#e1e100', // Color the shape will turn when intersects
              timeout: 1000,
              message: '<strong>Polygon draw does not allow intersections!<strong> (allowIntersection: false)' // Message that will show when intersect
            },
            shapeOptions: {
              color: '#bada55'
            }
          },
          circle: false, // Turns off this drawing tool
          rectangle: false,
          // {
          //       shapeOptions: {
          //         clickable: false
          //       }
          //     }
        },
        edit: {
          featureGroup: editableLayers,
          edit: false
        }
      };


      //Initialise the draw control and pass it the FeatureGroup of editable layers
      var drawControl = new L.Control.Draw(drawPluginOptions);
      map.addControl(drawControl);


      map.on('draw:created', function (e) {
        let type = e.layerType;
        let layer = e.layer;

        console.log("layer: " + layer);
        console.log("type: " + type);
        if (type === 'marker') {
          //layer.bindPopup('A popup!');
          alert("I don't work yet");
        } else if (type == 'polygon') {
          console.log("layer: " + layer.getLatLngs());
          console.log("layer: " + JSON.stringify(layer.toGeoJSON()));
          editableLayers.addLayer(layer);
          let lambdaURL = apiURL;
          lambdaURL += fieldsURLPath;
          console.log("lambdaURL: " + lambdaURL);
          fetch(lambdaURL, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(layer.toGeoJSON())
          })
            .then(function (response) {
              //console.log("response: " + response);
              return response.json();
            })
            .then(function (data) {
              console.log("data: " + JSON.stringify(data));
              featureSelection = layer;
              searchFields(recentSearchBoxValue);
              map.removeLayer(layer);
              //addDataToMap(data, "");
              map.fitBounds(layer.getBounds());
            })
            .catch((error) => {
              console.error('Error:', error);
            });

          console.log("feature added");
        }
      });

      map.on('draw:edited', function (e) {
        let layers = e.layers;
        layers.eachLayer(function (layer) {
          console.log("layer: " + layer.getLatLngs());
          console.log("layer: " + JSON.stringify(layer.toGeoJSON()));
          //do whatever you want; most likely save back to db
        });

        //should do a get field and see if activity is associated with it before allowing delete
        // Update db to save latest changes.
      });

      map.on('draw:deletestart', function (e) {
        console.log("draw:deletestart: e " + e);
        //alert("Delete is not allowed yet.  Deletes can be dangerous.");
      });

      map.on('draw:deleted', function (e) {
        console.log("draw:deleted: e: " + e);
        let layers = e.layers;
        layers.eachLayer(function (layer) {


          console.log("layer: " + layer.getLatLngs());
          console.log("layer: " + JSON.stringify(layer.toGeoJSON()));
          console.log("layer type: " + layer.toGeoJSON().geometry["type"]);
          console.log("field_id: " + layer.toGeoJSON().fieldId);
          console.log("activity count: " + layer.toGeoJSON().properties["activity_count"]);

          let fieldId = layer.toGeoJSON().fieldId;
          let activityCount = layer.toGeoJSON().properties["activity_count"];
          let type = layer.toGeoJSON().geometry["type"];
          if (fieldId != undefined && parseInt(fieldId) > 0 && activityCount != undefined && parseInt(activityCount) < 1 && type == 'Polygon') {
            console.log("this feature is eligible for deletion");
            editableLayers.removeLayer(layer);
            //do whatever you want; most likely save back to db
            // Update db to save latest changes.
            let lambdaURL = apiURL;
            lambdaURL += fieldsURLPath + "/" + fieldId;
            console.log("lambdaURL: " + lambdaURL);
            fetch(lambdaURL, {
              method: 'DELETE',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(fieldId)
            })
              .then(function (response) {
                //console.log("response: " + response);
                return response.json();
              })
              .then(function (data) {
                console.log("data: " + JSON.stringify(data));
                featureSelection = layer;
                searchFields(recentSearchBoxValue);
                map.removeLayer(layer);
                //addDataToMap(data, "");
                map.fitBounds(layer.getBounds());
              })
              .catch((error) => {
                console.error('Error:', error);
              });
            console.log("feature removed");
          } else {
            alert("This feature has activity attached.  It is not eligible for deletion. ");
          }
        });
      });

      //basemaps
      console.log("add mapbox basemap");
      let mapboxSatStreet = L.tileLayer(mapBoxURL, {
        attribution: '&copy; <a href="https://www.mapbox.com/about/maps/">Mapbox</a>, &copy; <a href="http://www.openstreetmap.org/about/">OpenStreetMap</a> and <a href="https://www.mapbox.com/map-feedback/#/-74.5/40/10">Improve this map</a>'
      });
      console.log("add OSM as basemap");
      let osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      });
      //go ahead and add this base layer now
      mapboxSatStreet.addTo(map);
      // let layer = new L.StamenTileLayer("toner");
      // let layer2 = new L.StamenTileLayer("watercolor");
      // let layer3 = new L.StamenTileLayer("terrain");

      basemaps = {
        // "Stamen Toner": layer,
        "Satellite Streets": mapboxSatStreet,
        "OpenStreetMap": osmLayer
        // "Stamen Terrain": layer3,
        // "Stamen Watercolor": layer2,
      };

      layerControl = L.control.layers(basemaps, overlays).addTo(map);

    } catch (e) {
      console.log("Exception caught: " + e);
    }
  };

  function searchFields(searchboxValue) {
    console.log("searchFields(searchboxValue: " + searchboxValue + ")");
    recentSearchBoxValue = searchboxValue;
    let lambdaURL = apiURL;
    clearFieldDiv();
    if (searchboxValue != undefined && searchboxValue.trim().length == 0) {
      lambdaURL += fieldsURLPath;
      console.log("lambdaURL: " + lambdaURL);
      fetch(lambdaURL, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      })
        .then(function (response) {
          //console.log("response: " + response);
          return response.json();
        })
        .then(function (data) {
          //console.log("data: " + data.toString());
          addDataToMap(data, searchboxValue);
          return JSON.stringify(data);
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    } else {
      lambdaURL += searchFieldsURLPath;
      console.log("lambdaURL: " + lambdaURL);
      let searchData = {search: searchboxValue};
      console.log("searchData" + JSON.stringify(searchData));
      fetch(lambdaURL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(searchData)
      })
        .then(function (response) {
          //console.log("response: " + response);
          return response.json();
        })
        .then(function (data) {
          //console.log("data: " + data.toString());
          addDataToMap(data, searchboxValue);
          return JSON.stringify(data);
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    }


  };

  function addDataToMap(data, searchboxvalue) {
    //console.log("addDataToMap: " + JSON.stringify(data) + ", searchboxvalue: " + searchboxvalue);
    console.log("addDataToMap ");
    //console.log("existing overlays: " + overlays.size);
    for (let name in overlays) {
      console.log("overlay: " + name);
      map.removeLayer(overlays[name]);
      layerControl.removeLayer(overlays[name]);
    }
    let growerLabel = "All Grower";
    if (searchboxvalue.trim().length > 0) {
      growerLabel = searchboxvalue.charAt(0).toUpperCase() + searchboxvalue.slice(1);
    }
    let growerFieldsLabel = growerLabel + " Fields";
    try {
      fieldsLayer = L.geoJSON(data, {
        style: fieldStyle,
        onEachFeature: fieldOnEachFeature
      });
      overlays[growerFieldsLabel] = fieldsLayer;
      layerControl.addOverlay(fieldsLayer, growerFieldsLabel);
      editableLayers.addLayer(fieldsLayer);
      //console.log(overlays);
      fieldsLayer.addTo(map);
      editableLayers.addTo(map);
      map.fitBounds(fieldsLayer.getBounds());
      if (featureSelection) {
        resetStyles();
        map.fitBounds(featureSelection.getBounds());
      }

      // console.log("fieldsLayer added");
    } catch (e) {
      console.log("Exception caught adding geoJson layer: " + e)
    }
  };

  function fieldStyle(feature) {
    return {
      fill: true,
      stroke: true,
      fillColor: '#e8fdfb',
      color: '#095d58',
      fillOpacity: 0.75,
      weight: 3,
      opacity: 1
    };
  };

  function fieldSelectedStyle(feature) {
    return {
      fill: true,
      stroke: true,
      fillColor: '#095d58',
      color: '#095d58',
      fillOpacity: 0.75,
      weight: 3,
      opacity: 1
    };
  };

  function resetStyles() {
    console.log("inside resetStyles");
    console.log("featureLayer: " + featureLayer);
    try {
      // if (featureLayer === radioactiveLayer) {
      //     featureSelection.setIcon(radioactiveIcon);
      // } else
      if (featureLayer === fieldsLayer) {
        console.log("resetting featureSelection");
        featureLayer.resetStyle(featureSelection);
      }
      // $("#countyDiv").html('<h3>Texas Counties and Features</h3><ul><li>Use the Layer Control to select basemap and overalys</li><li>Zoom to see additional basemap details</li><li>Click on a county, airport, park, railroad, or radioactive site to see feature-specific information</li></ul>');
    } catch (e) {
      console.error("exception caught in resetStyles: " + e);
    }
  };

  // handle click events on field features
  function fieldOnEachFeature(feature, layer) {
    try {
      layer.bindPopup("<b> Field ID: </b>" + feature.fieldId + "<br/> <b>Field Name: </b>" + feature.properties.fieldname + "<br/><b>Grower: </b>" + feature.properties.growername);

      layer.on({
        add: function () {
          layer.bringToBack();
        },
        remove: function () {
          // $("#legend").html("<br/>");
        },
        click: function (e) {
          try {
            if (featureSelection) {
              resetStyles();
            }
            e.target.setStyle(fieldSelectedStyle());
            featureSelection = e.target;
            featureLayer = fieldsLayer;
            // Insert some HTML with the feature name
            writeFieldDiv(feature);

            // stop click event from being propagated further
            L.DomEvent.stopPropagation(e);
          } catch (e) {
            console.log("Exception caught in county click function: " + e);
          }
        },
        mouseover: function (e) {
          try {
            // writeFieldDiv(feature);
          } catch (e) {
            console.error("Exception caught in county click function: " + e);
          }
        }
      });
    } catch (e) {
      log.error("Exception caught in countyOnEachFeature: " + e);
    }
  };

  function writeFieldDiv(currentFeature) {
    console.log("writeFieldDiv");
    $("#fieldDiv").html("");
    let propertiesObject = currentFeature.properties;
    let growerName = propertiesObject["growername"];
    let fieldName = propertiesObject["fieldname"];
    console.log("fieldName: " + fieldName);

    let fieldDivHTML = "<b>Grower: </b>" + growerName + "<br/><b>Field Name: </b>" + fieldName;

    // <i class="fa fa-pencil-square-o" aria-hidden="true" ></i>
    // <i class="fa fa-info-circle" aria-hidden="true"></i>
    console.log("fieldDivHTML: " + fieldDivHTML);
    $("#fieldDiv").html(fieldDivHTML);
    $("#fieldDetailsButton").css("display", "block");
    $("#fieldDetails").css("display", "none");
    $("#fieldActivity").css("display", "none");
    $("#fieldID").val(currentFeature.fieldId).trigger('input').trigger('change');

    console.log("fieldID value: " + $("#fieldID").val())
  }

  function clearFieldDiv() {
    console.log("clearFieldDiv");
    $("#fieldDiv").html("");
    $("#fieldDetailsButton").css("display", "none");
    $("#fieldDetails").css("display", "none");
    $("#fieldActivity").css("display", "none");
  }

  return {
    init: init,
    searchFields: searchFields
  }
})()

