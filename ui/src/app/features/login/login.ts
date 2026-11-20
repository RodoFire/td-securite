import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../core/auth/auth.service';

/**
 * Page de connexion. Elle ne sait pas comment on s'authentifie : elle passe le
 * login et le mot de passe à AuthService, puis va sur la page demandée au départ
 * (`?retour=`, posé par la guard) ou sur les comptes.
 */
@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, MatButtonModule, MatCardModule, MatFormFieldModule, MatInputModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Login {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  protected readonly formulaire = inject(FormBuilder).nonNullable.group({
    login: ['', Validators.required],
    motDePasse: ['', Validators.required],
  });
  protected readonly enCours = signal(false);
  protected readonly erreur = signal<string | null>(null);

  protected async connecter(): Promise<void> {
    if (this.formulaire.invalid || this.enCours()) {
      return;
    }
    this.enCours.set(true);
    this.erreur.set(null);
    const { login, motDePasse } = this.formulaire.getRawValue();
    try {
      await this.auth.connecter(login, motDePasse);
      this.auth.uponConnection()
      const retour = this.route.snapshot.queryParamMap.get('retour');
      await this.router.navigateByUrl(retour ?? '/comptes');
    } catch {
      this.erreur.set('Identifiant ou mot de passe incorrect.');
    } finally {
      this.enCours.set(false);
    }
  }
}
