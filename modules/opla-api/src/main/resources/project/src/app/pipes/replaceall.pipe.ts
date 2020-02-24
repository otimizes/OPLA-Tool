import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'replaceall'
})
export class ReplaceallPipe implements PipeTransform {

  transform(value: string, ...args: any[]): any {
    return value.replace(new RegExp(args[0], 'g'), args[1]);
  }

}
