// role.model.ts
export class Role {
    id: string;
    name: ERole;
  
    constructor(id: string, name: ERole) {
      this.id = id;
      this.name = name;
    }
  }
  
  // Enum ERole
  export enum ERole {
    ROLE_USER = 'ROLE_USER',
    ROLE_ADMIN = 'ROLE_ADMIN'
  }
  