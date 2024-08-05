import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'app/USERALLL/USERALL/_services/token-storage.service';

@Component({
    selector: 'user-cmp',
    moduleId: module.id,
    templateUrl: 'user.component.html'
})

export class UserComponent implements OnInit{
    currentUser: any;
    profileImgSrc!: string;
    lastName: string = '';
    constructor(private token: TokenStorageService) { }
   
    ngOnInit(){

        this.currentUser = this.token.getUser();
        this.extractLastNameFromEmail();
        // Retrieve profile image from local storage
        const savedProfileImg = localStorage.getItem('profileImg');
        if (savedProfileImg) {
          this.profileImgSrc = savedProfileImg;
        } else {
          this.profileImgSrc = 'assets/images/avatar.png';
        }
        
    
}    extractLastNameFromEmail() {
    if (this.currentUser && this.currentUser.email) {
        const email = this.currentUser.email;
        const atIndex = email.lastIndexOf('@');
        const lastDotBeforeAt = email.lastIndexOf('.', atIndex);
        if (lastDotBeforeAt !== -1 && atIndex !== -1) {
          this.lastName = email.substring(lastDotBeforeAt + 1, atIndex);
        }
      }
  
}


}