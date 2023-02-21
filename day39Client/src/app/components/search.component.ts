import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchForm!: FormGroup;

  constructor(private fb: FormBuilder, private router: Router){ }

  ngOnInit(): void {
      this.searchForm= this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      characterName: this.fb.control('', [Validators.required])
    })
  }

  submitSearch(): void {
    console.log(this.searchForm.value);
    const characterName = this.searchForm.value['characterName'];
    this.router.navigate([`search/${characterName}`])
  }
}
