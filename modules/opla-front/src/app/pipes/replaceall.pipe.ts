import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'replaceall'
})
export class ReplaceallPipe implements PipeTransform {

  transform(value: string, ...args: any[]): any {
    let split = value.split(args[0]);
    let toReturn = "";
    for (let str of split) {
      if (str.toLowerCase() === "pla" || split.length <= 1) {
        toReturn += str.toUpperCase() + args[1];
      } else {
        toReturn += this.capitalize(str.toLowerCase()) + args[1];
      }
    }

    return toReturn;
  }


  capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
}
