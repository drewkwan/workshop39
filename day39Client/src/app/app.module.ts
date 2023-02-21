import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './components/search.component';
import { CharacterListComponent } from './components/character-list.component';
import { CharacterDetailsComponent } from './components/character-details.component';
import { CommentsComponent } from './components/comments.component';

const appRoutes: Routes = [
  {path: '', component: SearchComponent},
  {path:'search/:characterName', component: CharacterListComponent},
  {path: 'characters/:characterId', component: CharacterDetailsComponent}
  
]

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    CharacterListComponent,
    CharacterDetailsComponent,
    CommentsComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes),
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
