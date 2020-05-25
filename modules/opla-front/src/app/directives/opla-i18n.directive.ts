import {Component, Directive, ElementRef, HostListener, Inject, Input, Renderer2} from '@angular/core';
import {OptimizationService} from "../services/optimization.service";
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";

@Directive({
  selector: '[oplaI18n]'
})
export class OplaI18nDirective {

  @Input("oplaI18n") oplaI18nTitle: string;
  i18n: any;
  context: any = {};
  oplaI18n: HTMLElement;
  offset = 10;
  delay = 200;
  placement: string = 'top';
  info: HTMLElement;

  constructor(private el: ElementRef, private renderer: Renderer2, private service: OptimizationService, public dialog: MatDialog) {
    console.log("foi ", this.oplaI18nTitle, this.i18n)
  }


  @HostListener('mouseenter') onMouseEnter() {
    this.i18n = OptimizationService.getI18n(this.oplaI18nTitle);
    if (!this.oplaI18n) {
      this.show();
    }
  }

  @HostListener('mouseleave') onMouseLeave() {
    if (this.oplaI18n) {
      this.hide();
    }
  }

  show() {
    this.create();
    this.setPosition();
    this.renderer.addClass(this.oplaI18n, 'ng-tooltip-show');
  }

  hide() {
    this.renderer.removeClass(this.oplaI18n, 'ng-tooltip-show');
    window.setTimeout(() => {
      this.renderer.removeChild(document.body, this.oplaI18n);
      this.renderer.removeChild(this.el.nativeElement, this.info);
      this.oplaI18n = null;
    }, this.delay);
  }

  create() {
    this.info = this.renderer.createElement("mat-icon");
    this.info.innerText = "help_outline";
    this.info.onclick = event => {
      this.infoClick(event, this.i18n);
    };

    this.oplaI18n = this.renderer.createElement('span');

    this.renderer.appendChild(
      this.oplaI18n,
      this.renderer.createText(this.i18n.value) // textNode
    );

    this.renderer.addClass(this.info, "mat-icon");
    this.renderer.addClass(this.info, "material-icons");
    this.renderer.addClass(this.info, "ng-tooltip-info");
    this.renderer.appendChild(
      this.el.nativeElement,
      this.info
    );


    this.renderer.appendChild(document.body, this.oplaI18n);

    this.renderer.addClass(this.oplaI18n, 'ng-tooltip');
    this.renderer.addClass(this.oplaI18n, `ng-tooltip-${this.placement}`);

    this.renderer.setStyle(this.oplaI18n, '-webkit-transition', `opacity ${this.delay}ms`);
    this.renderer.setStyle(this.oplaI18n, '-moz-transition', `opacity ${this.delay}ms`);
    this.renderer.setStyle(this.oplaI18n, '-o-transition', `opacity ${this.delay}ms`);
    this.renderer.setStyle(this.oplaI18n, 'transition', `opacity ${this.delay}ms`);
  }

  setPosition() {
    const hostPos = this.el.nativeElement.getBoundingClientRect();
    const tooltipPos = this.oplaI18n.getBoundingClientRect();
    const scrollPos = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;

    let top, left;

    if (this.placement === 'top') {
      top = hostPos.top - tooltipPos.height - this.offset;
      left = hostPos.left + (hostPos.width - tooltipPos.width) / 2;
    }

    if (this.placement === 'bottom') {
      top = hostPos.bottom + this.offset;
      left = hostPos.left + (hostPos.width - tooltipPos.width) / 2;
    }

    if (this.placement === 'left') {
      top = hostPos.top + (hostPos.height - tooltipPos.height) / 2;
      left = hostPos.left - tooltipPos.width - this.offset;
    }

    if (this.placement === 'right') {
      top = hostPos.top + (hostPos.height - tooltipPos.height) / 2;
      left = hostPos.right + this.offset;
    }

    this.renderer.setStyle(this.oplaI18n, 'top', `${top + scrollPos}px`);
    this.renderer.setStyle(this.oplaI18n, 'left', `${left}px`);
  }


  private infoClick(event: MouseEvent, i18n) {
    this.dialog.open(DialogTooltipInfo, {
      data: i18n
    })
  }
}

@Component({
  selector: 'dialog-tooltip-info',
  template: `
      <div style="display: flex; align-items: center;">
          <mat-icon style="margin:10px">info_outline</mat-icon>
          <span style="text-align: justify;">{{description}}</span>
      </div>
  `,
})
export class DialogTooltipInfo {
  description: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    console.log(data)
    this.description = data.description;
  }
}
