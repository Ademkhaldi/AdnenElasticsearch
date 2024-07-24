import { Component, OnInit } from '@angular/core';
import { AuthService } from '../_services/auth.service';
import { ERole } from '../user/role.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: any = {
    username: null,
    email: null,
    password: null,
    roles: [] // Initialize roles as an empty array
  };
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  profileImgSrc: string = ''; // Initialize with an empty string or a valid default URL
  availableRoles = Object.values(ERole); // Get available roles from ERole enum
  passwordFieldType: string = 'password'; // Field type for password input
  passwordToggleText: string = 'Afficher le mot de passe'; // Text for password toggle button


  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.profileImgSrc = 'assets/images/avatar.jpg'; // Use a valid URL or a default image
  }

  onSubmit(): void {
    const { username, email, password, roles } = this.form;
    const role = roles.length > 0 ? roles[0] : 'user'; // Default to 'user' if no role selected
    this.authService.register(username, email, password, role).subscribe(
      data => {
        console.log(data);
        this.isSuccessful = true;
        this.isSignUpFailed = false;
        // Save profile image to local storage
        localStorage.setItem('profileImg', this.profileImgSrc);
      },
      err => {
        this.errorMessage = err.error.message;
        this.isSignUpFailed = true;
      }
    );
  }

  onProfileImgClick(): void {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    fileInput.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.profileImgSrc = e.target.result;
      };
      reader.readAsDataURL(input.files[0]);
    }
  }

  
  registerUser(): void {
    this.form.roles = ['user'];
    this.onSubmit();
  }

  registerAdmin(): void {
    this.form.roles = ['admin'];
    this.onSubmit();
  }

  setPasswordVisibility(showPassword: boolean): void {
    this.passwordFieldType = showPassword ? 'text' : 'password';
  }


  togglePasswordVisibility(): void {
    this.passwordFieldType = (this.passwordFieldType === 'password') ? 'text' : 'password'; // Toggle password field type
    this.passwordToggleText = (this.passwordFieldType === 'password') ? 'Afficher le mot de passe' : 'Masquer le mot de passe'; // Toggle button text
  }    
}
