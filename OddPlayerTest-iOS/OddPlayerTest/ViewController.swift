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

class ViewController: UIViewController, UITextFieldDelegate {

  @IBOutlet var urlTextField: UITextField!
  @IBOutlet weak var playButton: UIButton!
  
  override func viewDidLoad() {
    super.viewDidLoad()
    self.urlTextField.addTarget(self, action: #selector(self.textFieldChanged), forControlEvents: .EditingChanged)
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
      videoURL = NSURL(string: urlString) else { break }
      
      let mediaItem = AVPlayerItem(URL: videoURL)
      let player = AVPlayer(playerItem: mediaItem)
      vc.player = player
      vc.player?.play()
    default: break
    }
  }
  
}

