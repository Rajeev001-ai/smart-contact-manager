console.log("admin js")

document.querySelector("#image_file_input")
        .addEventListener("change",function(event){

            let file=event.target.files[0];
            let reader = new FileReader();
            reader.onload = function(){

                document.querySelector("#upload_image_preview")
                .setAttribute("src",reader.result)
            }

            reader.readAsDataURL(file)
        })

let file=document.querySelector("#image_file_input")

let img=document.querySelector("#upload_image_preview")

file.addEventListener("click",function(){

    img.classList.add("contactImg")
})
