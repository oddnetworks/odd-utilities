//
//  ViewController.swift
//  OddPlayerTest
//
//  Created by Patrick McConnell on 5/16/16.
//  Copyright © 2016 Oddnetworks. All rights reserved.
//

import UIKit
import AVKit
import AVFoundation

private var myContext = 0

class MyAVPlayerItem: AVPlayerItem {
  
  var observer: UIViewController? = nil
  
  deinit {
    if observer != nil {
      self.removeObserver(observer!, forKeyPath: "status", context: &myContext)
    }
  }
}

class ViewController: UIViewController, UITextFieldDelegate {

  @IBOutlet var urlTextField: UITextField!
  @IBOutlet weak var playButton: UIButton!

  var mediaItem: MyAVPlayerItem?
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.urlTextField.addTarget(self, action: #selector(self.textFieldChanged), forControlEvents: .EditingChanged)
    
    NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(self.newAVPlayerError), name: AVPlayerItemNewErrorLogEntryNotification, object: nil)
  }
  
  override func viewWillDisappear(animated: Bool) {
    super.viewWillDisappear(animated)
//    mediaItem?.removeObserver(self, forKeyPath: "status", context: &myContext)
  }
  

  override func didReceiveMemoryWarning() {
    super.didReceiveMemoryWarning()
    // Dispose of any resources that can be recreated.
  }

  @IBAction func playButtonPressed(sender: AnyObject) {
    self.performSegueWithIdentifier("playVideoSegue", sender: self)
  }

  func textFieldChanged() {
    self.playButton.enabled = !(self.urlTextField.text?.isEmpty)!
  }
  
  override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
    switch segue.identifier {
    case .Some("playVideoSegue"):
      guard let vc = segue.destinationViewController as? AVPlayerViewController,
      urlString = self.urlTextField.text,
      videoURL = NSURL(string: urlString) else {
        showAlert("Unable to create url")
        break
      }
      
      mediaItem = MyAVPlayerItem(URL: videoURL)
      
      if mediaItem == nil {
        showAlert("Unable to create mediaItem")
        break
      }
      
      mediaItem!.observer = self
      mediaItem!.addObserver(self, forKeyPath: "status", options: .New, context: &myContext)
      let player = AVPlayer(playerItem: mediaItem!)
      vc.player = player
      vc.player?.play()
    default: break
    }
  }

  override func observeValueForKeyPath(keyPath: String?, ofObject object: AnyObject?, change: [String : AnyObject]?, context: UnsafeMutablePointer<Void>) {
    if context == &myContext {
      if keyPath == "status" {
        guard let theChange = change,
          statusInt = theChange[NSKeyValueChangeNewKey] as? Int,
          status = AVPlayerItemStatus(rawValue: statusInt) else { return }
        if status == .Failed {
          showAlert("Error playing video")
        }
      }
    }
  }
  
  func showAlert(message: String) {
    let alert = UIAlertController(title: "Error", message: message, preferredStyle: .Alert)
    
    let ok = UIAlertAction(title: "OK", style: .Default, handler: nil)
    alert.addAction(ok)
  
    guard let item = self.mediaItem,
      log = item.errorLog() else {
        self.view.window?.rootViewController?.presentViewController(alert, animated: true, completion: {})
        return
    }
    
    let showLog = UIAlertAction(title: "Show Error Log", style: .Default) { (action) in
      print(log)
    }
        
    alert.addAction(showLog)
    
    self.view.window?.rootViewController?.presentViewController(alert, animated: true, completion: {})
  }
  
  func newAVPlayerError() {
    print("Error")
  }
}
