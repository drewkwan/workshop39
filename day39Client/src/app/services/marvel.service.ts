import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, lastValueFrom } from 'rxjs';
import { MarvelCharacter } from '../models';

@Injectable({
  providedIn: 'root'
})
export class MarvelService {

          //URL https://gateway.marvel.com:443/v1/public/characters?
        //nameStartsWith=??
        //limit=10
        //ts=??
        //apiKey=??
        //hash=??

  marvelCharacter!: MarvelCharacter;

  constructor(private http: HttpClient) { }

  getCharacters(characterName: string): Promise<any> {
    const params = new HttpParams()
                .set("name", characterName)
    return firstValueFrom(this.http.get("/api/characters", {params: params})); 
    
  }

  getCharacterById(characterId:number):Promise<any> {
    const params = new HttpParams()
                .set("characterId", characterId);
    return firstValueFrom(this.http.get(`/api/character/${characterId}`, {params:params}));
  }

  postComments(characterId: number, comments:string) {

    const requestBody = {
      comments: comments
    }
    const params = new HttpParams().set("comments", comments);
    return lastValueFrom(this.http.post(`/api/character/${characterId}`, requestBody));
  }

}
