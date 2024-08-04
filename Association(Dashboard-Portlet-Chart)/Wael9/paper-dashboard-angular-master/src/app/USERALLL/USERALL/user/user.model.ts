import { Role } from "./role.model";

export class User {
    id: string; // Assurez-vous que l'ID est toujours une chaîne et ne peut pas être indéfinie
    username: string;
    email: string;
    password: string;
    roles: Set<Role>;
  
    constructor() {
      this.id = '';
      this.username = '';
      this.email = '';
      this.password = '';
      this.roles = new Set<Role>();
    }
  }
  