import { Component, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ChatResponse } from '../chat-response';

@Component({
  selector: 'app-simple-chat',
  imports: [
    MatCardModule,
    MatToolbarModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    FormsModule,
    NgClass,
  ],
  templateUrl: './simple-chat.html',
  styleUrl: './simple-chat.scss',
})
export class SimpleChat {
  messages = signal<ChatResponse[]>([
    { message: 'Olá, como posso te ajudar?', isBot: true },
  ]);

  userInput = '';

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput !== '') {
      this.updateMessages(this.userInput, false);
      this.userInput = '';
      this.simulateResponse();
    }
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private updateMessages(message: string, isBot: boolean) {
    this.messages.update((messages) => [...messages, { message, isBot }]);
  }

  private simulateResponse() {
    setTimeout(() => {
      const response = 'Está é uma resposta simulada do Chat de IA';
      this.updateMessages(response, true);
    }, 2000);
  }
}
