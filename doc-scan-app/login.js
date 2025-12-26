const { ipcRenderer } = require('electron')

let btnlogin;
let user; 
let clave;

window.onload = function() { 
  user = document.getElementById("user")
  clave = document.getElementById("clave")
  btnlogin = document.getElementById("login")
  //user.value = 'asanchez'
  //clave.value = '1234567890'
  btnlogin.onclick = function(){
    
   const obj = {user:user.value, clave:clave.value }

    ipcRenderer.invoke("login", obj)
  }
}