var leafletMap = (function () {
  'use strict'

  let map;
  let fieldsLayer;
  let basemaps = {};
  let overlays = {};
  let featureSelection;
  let featureLayer;
  let layerControl;

  function init() {
    console.log("in leafletMap init()");

    // create map and set center and zoom level
    try {
      map = new L.map('mapid');
      map.setView([32.112355, -98.537303], 12);
      map.options.minZoom = 5;
      // map.options.maxZoom = 12;

      //basemaps
      console.log("add OSM as basemap");
      let osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
      });
      //go ahead and add this base layer now
      osmLayer.addTo(map);
      // let layer = new L.StamenTileLayer("toner");
      // let layer2 = new L.StamenTileLayer("watercolor");
      // let layer3 = new L.StamenTileLayer("terrain");

      basemaps = {
        // "Stamen Toner": layer,
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
    let url = "https://m3e7z1ahi2.execute-api.us-east-1.amazonaws.com/Prod/dbconnect";
    fetch(url)
      .then(function (response) {
        //console.log("response: " + response);
        return response.json();
      })
      .then(function (data) {
        //console.log("data: " + data.toString());
        addDataToMap(data);

      });
  };

  function addDataToMap(data) {
    console.log("addDataToMap: " + JSON.stringify(data));
    fieldsLayer = L.geoJSON(data, {
      style: fieldStyle,
      onEachFeature: fieldOnEachFeature
    });
    layerControl.addOverlay(fieldsLayer, "Grower Fields");
    //console.log(overlays);
    fieldsLayer.addTo(map);
    // console.log("fieldsLayer added");
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

  // handle click events on county features
  function fieldOnEachFeature(feature, layer) {
    try {
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
            // writeCountyDiv(feature);

            //reset the summary label for all other features
            // $("#summaryLabel").html('<br/>');
            // $("#legend").html("<br/>");
            // stop click event from being propagated further
            L.DomEvent.stopPropagation(e);
          } catch (e) {
            console.log("Exception caught in county click function: " + e);
          }
        },
        mouseover: function (e) {
          try {
            // writeCountyDiv(feature);
          } catch (e) {
            console.error("Exception caught in county click function: " + e);
          }
        }
      });
    } catch (e) {
      log.error("Exception caught in countyOnEachFeature: " + e);
    }
  };

  return {
    init: init,
    searchFields: searchFields
  }
})()

