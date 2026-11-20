import {HttpClient, HttpParams, HttpParameterCodec} from '@angular/common/http';
import {inject} from '@angular/core';
import {Injectable, signal} from '@angular/core';
import {firstValueFrom} from 'rxjs';

export const API = 'http://localhost:8080/api';

export interface Utilisateur {
  readonly login: string;
  readonly roles: readonly string[];
}

/**
 * Garde l'utilisateur connecté. Squelette du TD : à compléter en partie 2.
 */
@Injectable({providedIn: 'root'})
export class AuthService {
  private http = inject(HttpClient);
  readonly utilisateur = signal<Utilisateur | null>(null);

  private login?: string;
  private motDePasse?: string;

  private connecte: boolean = false;

  /**
   * Vérifie les identifiants auprès du serveur (GET /api/me avec l'en-tête
   * Basic) et mémorise l'utilisateur. Doit échouer (throw) si le serveur répond 401.
   */
  async connecter(login: string, motDePasse: string): Promise<void> {
    this.login = login;
    this.motDePasse = motDePasse;
    console.log("Attempting to connect")
    return firstValueFrom(this.http.get<void>(`${API}/me`))
  }

  deconnecter(): void {
    // TODO : oublier les identifiants et l'utilisateur.
    this.connecte = false;
    this.motDePasse = undefined;
    this.login = undefined;
  }

  basic(): string | undefined {
    if (this.login && this.motDePasse) {
      return btoa(this.login + ":" + this.motDePasse);
    }
    return undefined;
  }

  uponConnection() : void {
    this.connecte = true;
  }

  estConnecte(): boolean {
    return this.connecte
  }
}
