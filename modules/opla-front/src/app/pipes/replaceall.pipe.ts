import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'replaceall'
})
export class ReplaceallPipe implements PipeTransform {

  transform(value: string, ...args: any[]): any {
    let split = value.split(args[0]);
    let toReturn = "";
    let i = 0;
    for (let str of split) {
      let splitter = args[1];
      if (str.toLowerCase() === "feature" && split[i + 1].toLowerCase() === "driven") {
        splitter = "-";
      }
      if (str.toLowerCase() === "pla" || split.length <= 1) {
        toReturn += str.toUpperCase() + splitter;
      } else if (value.toLowerCase().includes("asp")) {
        toReturn += str.toUpperCase() + splitter;
      } else {
        toReturn += this.capitalize(str.toLowerCase()) + splitter;
      }
      i++;
    }

    return toReturn;
  }


  capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
  }
}
