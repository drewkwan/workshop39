import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { MarvelCharacter } from '../models';
import { MarvelService } from '../services/marvel.service';

@Component({
  selector: 'app-character-list',
  templateUrl: './character-list.component.html',
  styleUrls: ['./character-list.component.css']
})
export class CharacterListComponent implements OnInit, OnDestroy {

  routeSub$!: Subscription;
  characterName!: string;
  marvelCharacter!: string;
  marvelCharacters: MarvelCharacter[] =[];
  
  constructor(private activatedRoute: ActivatedRoute, private marvelSvc: MarvelService, private router: Router) { }

  ngOnInit(): void {
    console.log(this.activatedRoute);

    console.log(this.activatedRoute.snapshot.params['characterName'])

    this.routeSub$= this.activatedRoute.params.subscribe(params=> {
      this.characterName=params['characterName'];
    })
    this.getCharacterList();
    
  }

  getCharacterList() {
    this.marvelSvc.getCharacters(this.characterName).then(response => {
      this.marvelCharacters= response;
    }).catch(error => {
      console.log(error);
    })
  }

  viewDetails(characterId: number) {
    console.log(characterId);
    this.router.navigate([`characters/${characterId}`])
  }

  ngOnDestroy() {
    this.routeSub$.unsubscribe;
  }

}
