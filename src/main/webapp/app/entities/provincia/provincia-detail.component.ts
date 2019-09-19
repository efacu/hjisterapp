import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProvincia } from 'app/shared/model/provincia.model';

@Component({
  selector: 'jhi-provincia-detail',
  templateUrl: './provincia-detail.component.html'
})
export class ProvinciaDetailComponent implements OnInit {
  provincia: IProvincia;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ provincia }) => {
      this.provincia = provincia;
    });
  }

  previousState() {
    window.history.back();
  }
}
