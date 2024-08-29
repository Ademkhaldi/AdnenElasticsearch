// src/app/shared/color.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ColorService {
  private labelColors: { [key: string]: string } = {};

  private generateRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  getColorForLabel(label: string): string {
    if (!this.labelColors[label]) {
      this.labelColors[label] = this.generateRandomColor();
    }
    return this.labelColors[label];
  }
}
