import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ImageService {
  private baseUrl = 'http://your-server-url.com/assets/images/'; // URL de base pour les images

  getProfileImageUrl(imageName: string): string {
    return `${this.baseUrl}${imageName}`;
  }
}
