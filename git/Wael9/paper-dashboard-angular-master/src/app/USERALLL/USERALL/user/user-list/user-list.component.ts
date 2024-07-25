import { Component, OnInit } from "@angular/core";
import { User } from "../user.model";
import { UserService } from "../../_services/user.service";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "../../_services/auth.service";
import { Role } from "../role.model";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  id: string = '';
  public users: User[] = [];
  user: User = new User();
  public isAdmin: boolean = false;
  public isUser: boolean = false;
  currentUser: User | null = null; // Déclarez la variable currentUser de type User ou null

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {

    this.currentUser = this.authService.getCurrentUser();
    // Vérifiez si l'utilisateur a le rôle admin
    this.isAdmin = this.authService.hasRole('ROLE_ADMIN');
    // Vérifiez si l'utilisateur a le rôle user
    this.isUser = this.authService.hasRole('ROLE_USER');
    
    if (this.isAdmin) {
      // Si l'utilisateur est admin, chargez tous les utilisateurs
      this.reloadData();
    } else if (this.isUser) {
      // Sinon, chargez les données de l'utilisateur connecté
      this.reloadData2();
    }
  }

  reloadData() {
    this.userService.getAllUsers().subscribe(data => {
      this.users = data;
      console.log(data);
    });
  }

  reloadData2() {
    const currentUser = this.authService.getCurrentUser(); // Assuming you have a method to get the current logged-in user
    if (currentUser && currentUser.id) {
      this.userService.retrieveUser(currentUser.id)
        .subscribe(data => {
          console.log(data);
          this.user = data;
        }, error => console.log(error));
    }
  }

  updateUser(id: string | undefined) {
    if (id) {
      this.router.navigate(['UpdateUserComponent', id]);
    }
  }

  deleteUser(id: string | undefined) {
    if (id) {
      this.userService.deleteUser(id).subscribe(
        data => {
          console.log(data);
          this.reloadData();
        },
        error => console.log(error)
      );
    }
  }
  hasUserRole(user: User): boolean {
    const rolesArray = Array.from(user.roles); // Convertir l'ensemble de rôles en tableau
    return rolesArray.some(role => role.name === 'ROLE_USER');
  }
  

  hasAdminRole(user: User): boolean {
    const rolesArray = Array.from(user.roles); // Convertir l'ensemble de rôles en tableau
    return rolesArray.some(role => role.name === 'ROLE_ADMIN');
  }

  
  getRoleNames(roles: Set<Role>): string {
    return Array.from(roles).map(role => role.name).join(', ');
  }
}
