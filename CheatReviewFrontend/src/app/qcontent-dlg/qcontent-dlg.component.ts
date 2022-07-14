import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'app-qcontent-dlg',
  templateUrl: './qcontent-dlg.component.html',
  styleUrls: ['./qcontent-dlg.component.css']
})
export class QContentDlgComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: string) {
  }

  ngOnInit() {
  }

}
