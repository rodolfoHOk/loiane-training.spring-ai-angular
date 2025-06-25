import {
  Component,
  ElementRef,
  inject,
  signal,
  ViewChild,
} from '@angular/core';
import { NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ChatResponse } from '../chat-response';
import { ChatService } from '../chat-service';
import { catchError, of } from 'rxjs';

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
  @ViewChild('chatHistory')
  private chatHistory!: ElementRef;

  private chatService = inject(ChatService);

  local = false;

  messages = signal<ChatResponse[]>([
    { message: 'Olá, como posso te ajudar?', isBot: true },
  ]);

  userInput = '';
  isLoading = false;

  sendMessage() {
    this.trimUserMessage();
    if (this.userInput !== '' && !this.isLoading) {
      this.updateMessages(this.userInput, false);
      this.isLoading = true;
      if (this.local) {
        this.simulateResponse();
      } else {
        this.sendChatMessage();
      }
    }
  }

  private trimUserMessage() {
    this.userInput = this.userInput.trim();
  }

  private updateMessages(message: string, isBot: boolean) {
    this.messages.update((messages) => [...messages, { message, isBot }]);
    this.scrollToBottom();
  }

  private simulateResponse() {
    setTimeout(() => {
      const response = 'Está é uma resposta simulada do Chat de IA';
      this.updateMessages(response, true);
      this.userInput = '';
      this.isLoading = false;
    }, 2000);
  }

  private sendChatMessage() {
    this.chatService
      .sendChatMessage(this.userInput)
      .pipe(
        catchError(() => {
          this.updateMessages(
            'Desculpe, não posso processar sua solicitação no momento.',
            true
          );
          this.isLoading = false;
          return of();
        })
      )
      .subscribe((response) => {
        this.updateMessages(response.message, true);
        this.userInput = '';
        this.isLoading = false;
      });
  }

  private scrollToBottom() {
    try {
      this.chatHistory.nativeElement.scrollTop =
        this.chatHistory.nativeElement.scrollHeight;
    } catch (err) {
      console.error(err);
    }
  }
}
