find . -type f -print0 | while IFS= read -r -d $'\0' file; do
    ff=${file/notation/}
    ff=${ff/uml/}
    ff=${ff/di/}
    ff=${ff/\.\//}
    echo $ff
    sed -i "s/href=\".*\_TEMP\_.*\./href=\"$ff/gi" "$file"
    echo "$file"
done