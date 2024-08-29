import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../_services/user.service';
import { User } from '../user.model';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']  // Correction ici
})
export class UpdateUserComponent implements OnInit {

  id: string = '';
  user: User = new User();
  updator_id: string = 'updator'; // Nouveau champ updator_id

  constructor(private route: ActivatedRoute, private router: Router,
              private userService: UserService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.userService.retrieveUser(this.id)
      .subscribe(data => {
        console.log(data);
        this.user = data;
      }, error => console.log(error));
  } 

  updateUser() {
    const updateData = {
      ...this.user, // Copier toutes les autres propriétés de l'utilisateur
    };
  
    this.userService.updateUser(this.id, updateData).subscribe(
      (data) => {
        console.log(data);
        this.gotoList();
      },
      (error) => {
        console.log(error);
      }
    );
  }

  onSubmit() {
    this.updateUser();
  }

  gotoList() {
    this.router.navigate(['/user-list']);  // Correction ici pour correspondre à votre route
  }

  cancelUpdateUser() {
    this.gotoList();  // Naviguer vers la liste des utilisateurs
  }
}
