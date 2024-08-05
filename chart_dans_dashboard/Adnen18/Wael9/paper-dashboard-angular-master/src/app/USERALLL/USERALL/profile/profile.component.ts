import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from '../_services/token-storage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;
  profileImgSrc!: string;

  constructor(private token: TokenStorageService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    // Retrieve profile image from local storage
    const savedProfileImg = localStorage.getItem('profileImg');
    if (savedProfileImg) {
      this.profileImgSrc = savedProfileImg;
    } else {
      this.profileImgSrc = 'assets/images/avatar.png';
    }
  }
}
