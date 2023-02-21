import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { MarvelCharacter } from '../models';
import { MarvelService } from '../services/marvel.service';

@Component({
  selector: 'app-character-details',
  templateUrl: './character-details.component.html',
  styleUrls: ['./character-details.component.css']
})
export class CharacterDetailsComponent implements OnInit, OnDestroy {

  commentsForm!: FormGroup;
  marvelCharacter!: MarvelCharacter;
  routeSub$!: Subscription;
  characterId!: number
  uploadStatus!: string;

  constructor(private marvelSvc: MarvelService, private activatedRoute: ActivatedRoute, private fb: FormBuilder) { }

  ngOnInit(): void {
    //create form
    this.commentsForm=this.createForm();
    //intialise character details
    console.log(this.activatedRoute)
    this.routeSub$= this.activatedRoute.params.subscribe(params => {
      this.characterId=params['characterId'];
    })
    this.getCharacterDetails();
  }

  createForm(): FormGroup {
    return this.fb.group({
      comments: this.fb.control('', [Validators.required])
    })
  }

  getCharacterDetails() {
    this.marvelSvc.getCharacterById(this.characterId).then(response => {
      this.marvelCharacter=response;
    }).catch(error=> {
      console.log(error);
    })
  }

  submitComments(characterId: number) {
    console.log(characterId)
    console.log(this.commentsForm.get('comments')?.value);
    const comments = this.commentsForm.get('comments')?.value;
    this.marvelSvc.postComments(characterId, comments).then(response=> {
      console.log(response)
      this.uploadStatus ="nice"
    }).catch(error=>{
      console.error(error);
      this.uploadStatus = "notnice"
    }) 

    this.ngOnInit();

  }

  ngOnDestroy(): void {
    this.routeSub$.unsubscribe();
  }



}
