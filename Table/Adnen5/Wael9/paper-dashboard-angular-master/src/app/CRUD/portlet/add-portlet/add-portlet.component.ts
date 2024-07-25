import { Component, OnInit, Output,EventEmitter  } from '@angular/core';
import { Portlet } from '../portlet.model';
import { PortletService } from '../service/portlet.service';
import { Router } from '@angular/router';
import { User } from 'app/USERALLL/USERALL/user/user.model';
import { AuthService } from 'app/USERALLL/USERALL/_services/auth.service';
import { UserService } from 'app/USERALLL/USERALL/_services/user.service';

@Component({
  selector: 'app-add-portlet',
  templateUrl: './add-portlet.component.html',
  styleUrls: ['./add-portlet.component.scss']
})
export class AddPortletComponent implements OnInit {
  portlet: Portlet = {
    row: '',
    column: '',
  };

  updator_id:string;
  submitted = false;
  public users: User[] = [];
  user: User = new User();
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null
  creator_id: string ; // Nouveau champ creator_id
  navbarTitle: string = 'List'; // Provide a default value for navbarTitle
  constructor(private portletService: PortletService, private router: Router,private authService: AuthService,private userService: UserService) { }
  ngOnInit(): void {
  this.reloadData2();
  }


  reloadData2() {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      this.creator_id = currentUser.id;
      this.userService.retrieveUser(currentUser.id)
        .subscribe(
          data => {
            console.log(data);
            this.user = data;
            this.creator_id = this.user.username; // Update creator_id with the retrieved username
          },
          error => console.log(error)
        );
    }
  }

  savePortlet(): void {
   
   
    if (this.portlet.row.length === 0 ) {
      this.showErrorMessage('row', "row ne peut pas être vide");
      return;
    }
   
    if (this.portlet.column.length === 0 ) {
      this.showErrorMessage('column', "column ne peut pas être vide");
      return;
    }

    const data = {
      row: this.portlet.row,
      column: this.portlet.column,
      creator_id: this.creator_id, // Add creator_id when saving the datasource
      updator_id:this.creator_id

    };

    this.portletService.createPortlet(data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.submitted = true;
        },
        error: (e) => {
          console.error(e);
          // Handle errors appropriately
        }
      });
  }
  showErrorMessage(inputId: string, message: string): void {
    const inputElement = document.getElementById(inputId);
    const errorDiv = inputElement.nextElementSibling;
    if (errorDiv && errorDiv.classList.contains('text-danger')) {
      errorDiv.textContent = message;
    } else {
      const div = document.createElement('div');
      div.textContent = message;
      div.classList.add('text-danger');
      inputElement.insertAdjacentElement('afterend', div);
    }
  }
  newPortlet(): void {
    this.submitted = false;
    this.portlet = {
      row: '',
      column: ''
    };
  }

  gotoList() {
    this.router.navigate(['/getAllPortlets']); // Make sure the URL is correct for the list of datasources
  }
}
