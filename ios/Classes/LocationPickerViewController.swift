//
//  LocationPickerViewController.swift
//  location_picker_plugin
//
//  Created by Alejandro Sanchez on 4/9/18.
//

import UIKit
import GoogleMaps

class LocationPickerViewController: UIViewController, GMSMapViewDelegate, CLLocationManagerDelegate {
    
    var marker : GMSMarker?
    let geocoder = GMSGeocoder()
    var geocoderResult : GMSReverseGeocodeResult?
    var selectedPosition : CLLocationCoordinate2D = CLLocationCoordinate2D(latitude: 0.0, longitude: 0.0)
    var locationManager = CLLocationManager()
    var locationFirstTime : Bool = true
    var useGeoCoder : Bool = true
    var mapView : GMSMapView!
    var closeIfLocationDenied : Bool = false
    var enableMylocation : Bool = true
    
    override func loadView() {
        
        let args : [String : AnyObject?]  = SwiftLocationPickerPlugin.call?.arguments as! [String : AnyObject?]
        
        closeIfLocationDenied = args["closeIfLocationDenied"] as! Bool
        useGeoCoder = args["useGeoCoder"] as! Bool
        enableMylocation = args["enableMyLocation"] as! Bool
        
        // NAV BAR
        title = args["titleText"] as? String

        let cancelBtn = UIBarButtonItem(title: args["iosBackButtonText"] as? String, style: UIBarButtonItemStyle.done, target: self, action: #selector(close))
        let selectBtn = UIBarButtonItem(title: args["iosSelectButtonText"] as? String, style: UIBarButtonItemStyle.done, target: self, action: #selector(selectLocation))
        
        self.navigationItem.rightBarButtonItem = selectBtn
        self.navigationItem.leftBarButtonItem = cancelBtn
        
        // MAP INIT
        let camera = GMSCameraPosition.camera(withLatitude: selectedPosition.latitude, longitude: selectedPosition.longitude, zoom: 14.0)
        mapView = GMSMapView.map(withFrame: CGRect.zero, camera: camera)
        mapView.delegate = self
        view = mapView
        
        // USER LOCATION
        locationManager = CLLocationManager()
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.distanceFilter = 50
        locationManager.startUpdatingLocation()
        locationManager.delegate = self
        
        // Creates a marker in the center of the map.
        marker = GMSMarker()
        marker!.position = CLLocationCoordinate2D(latitude: (args["initialLat"] as? Double)!, longitude: (args["initialLong"] as? Double)!)
        marker!.map = mapView
    }
    
    @objc func close()
    {
        navigationController?.dismiss(animated: true, completion: sendCancelMessage)
    }
    
    @objc func selectLocation()
    {
        if useGeoCoder
        {
            geocoder.reverseGeocodeCoordinate(selectedPosition) { (response: GMSReverseGeocodeResponse?, error: Error?) in
                if let res : GMSReverseGeocodeResponse = response
                {
                    self.geocoderResult = res.firstResult()
                    self.navigationController?.dismiss(animated: true, completion: self.sendSelectedMessage)
                }
                else if let e : Error = error
                {
                    print(e.localizedDescription)
                }
            }
        }
        else
        {
            navigationController?.dismiss(animated: true, completion: sendSelectedMessage)
        }
    }
    
    func sendCancelMessage()
    {
        var response : [String : Double] = [:]
        response["cancel"] = 0.0
        SwiftLocationPickerPlugin.result!(response)
    }
    
    func sendLocationDenied(message : String)
    {
        if closeIfLocationDenied {
            var response : [String : String] = [:]
            response["locationDenied"] = message
            SwiftLocationPickerPlugin.result!(response)
        }
    }
    
    func sendSelectedMessage()
    {
        var response : [String : AnyObject?] = [:]
        response["latitude"] = selectedPosition.latitude as AnyObject
        response["longitude"] = selectedPosition.longitude as AnyObject
        
        if let geo : GMSReverseGeocodeResult = geocoderResult
        {
            response["administrativeArea"] = geo.administrativeArea as AnyObject
            response["country"] = geo.country as AnyObject
            response["locality"] = geo.locality as AnyObject
            response["subLocality"] = geo.subLocality as AnyObject
            response["postalCode"] = geo.postalCode as AnyObject
            response["thoroughfare"] = geo.thoroughfare as AnyObject
            
            if geo.lines!.count > 0
            {
                var counter = 0
                for line in geo.lines!
                {
                    let key = "line\(counter)"
                    response[key] = line as AnyObject
                    counter += 1
                }
            }
        }
        
        SwiftLocationPickerPlugin.result!(response)
    }
    
    func mapView(_ mapView: GMSMapView, didChange position: GMSCameraPosition) {
        selectedPosition = position.target
        marker?.position = selectedPosition
    }
    
    
    // USER LOCATION HANDLING

    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        let location: CLLocation = locations.last!
        if locationFirstTime
        {
            selectedPosition = location.coordinate
            locationFirstTime = false
            let camera = GMSCameraPosition.camera(withLatitude: selectedPosition.latitude, longitude: selectedPosition.longitude, zoom: 14.0)
            mapView.animate(to: camera)
        }
    }
    
    // Handle authorization for the location manager.
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
            
        case .restricted:
            print("Location access was restricted.")
            sendLocationDenied(message: "Location access was restricted.")
        case .denied:
            print("User denied access to location.")
            sendLocationDenied(message: "User denied access to location.")
        case .notDetermined:
            print("Location status not determined.")
            sendLocationDenied(message: "Location status not determined.")
            
        case .authorizedAlways: fallthrough
        case .authorizedWhenInUse:
            print("Location access granted.")
            mapView.isMyLocationEnabled = enableMylocation
        }
    }
}
