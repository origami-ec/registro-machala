const { app, BrowserWindow, ipcMain, Notification, net, dialog } = require('electron');
const crypto = require('crypto');
const path = require('path');
const http = require('http')
const storage = require('electron-json-storage');
const console = require('console');

const child_process = require('child_process');




const INCREMENT = 0.03
const INTERVAL_DELAY = 100 // ms

app.setAppUserModelId("Digitalización OrigamiGT")


/**
 * IP PARA LA FIRMA LOCAL 
 * http://127.0.0.1:8712/bcbg/api/firmaElectronica/consultarTokens
 */

const hostFirma = '127.0.0.1';
const portFirma = 8712;
var urlPathFirma = '/bcbg/api/firmaElectronica/';

/**
 * IP DEL SISTEMA PRINCIPAL
 * http://127.0.0.1:8713/servicios/bcbg/api/iniciarSesion
 */

// const hostApp = '192.168.4.30'
const hostApp = '192.188.3.49'
const portApp = 8713;
var urlPathApp = '/servicios/';


let winP12;
let winlogin;
let winhome;
let winToken;
let winScan;
let winEstampado;
/**
 * Se crean todas las pantallas
 */

function p12Window() {
    winP12 = new BrowserWindow({
        width: 1050,
        height: 850,
        icon: path.join(__dirname, '/resources/images/origami.png'),
        webPreferences: {
            preload: path.join(__dirname, 'index.js')
        }
    })
    winP12.removeMenu(true)
    winP12.loadFile('index.html')
}

function loginWindow() {
    winlogin = new BrowserWindow({
        width: 800,
        height: 600,
        icon: path.join(__dirname, '/resources/images/origami.png'),
        webPreferences: {
            preload: path.join(__dirname, 'login.js')
        }
    })
    winlogin.removeMenu(true)
    winlogin.loadFile('login.html')
}

function homeWindow() {
    winhome = new BrowserWindow({
        width: 800,
        height: 800,
        icon: path.join(__dirname, '/resources/images/origami.png'),
        webPreferences: {
            preload: path.join(__dirname, 'home.js')

        }
    })
    winhome.removeMenu(true)
    winhome.loadFile('home.html')
}

function tokenWindow() {
    winToken = new BrowserWindow({
            width: 800,
            height: 800,
            icon: path.join(__dirname, '/resources/images/origami.png'),
            webPreferences: {
                preload: path.join(__dirname, 'token.js')
            }
        })
        //winToken.removeMenu(true)
    winToken.loadFile('token.html')
}


function scanWindow() {
    winScan = new BrowserWindow({
        width: 800,
        height: 800,
        icon: path.join(__dirname, '/resources/images/origami.png'),
        webPreferences: {
            preload: path.join(__dirname, 'scan.js')

        }
    })
    winScan.webContents.openDevTools()
    winScan.removeMenu(true)
    winScan.loadFile('scan.html')
}


function estampadoWindow() {
    winEstampado = new BrowserWindow({
            width: 600,
            height: 970,
            maximizable: false,
            icon: path.join(__dirname, '/resources/images/origami.png'),
            webPreferences: {
                preload: path.join(__dirname, 'estampado.js')
            }
        })
        ///winEstampado.webContents.openDevTools()
    winEstampado.setResizable(false);
    winEstampado.removeMenu(true)
    winEstampado.loadFile('estampado.html')
}


app.whenReady().then(loginWindow)
//app.whenReady().then(p12Window)
// app.whenReady().then(homeWindow)
    //app.whenReady().then(tokenWindow)
    //app.whenReady().then(scanWindow)

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }
})

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        p12Window()
    }
})

//FIN DE PANTALLAS 

/**
 * Funciones desde otras pantallas 
 */

ipcMain.handle('login', (event, obj) => {
    validatelogin(obj)
});

ipcMain.handle('firmaFrame', (event) => {
    p12Window()
    winP12.show()
});

ipcMain.handle('tokenFrame', (event) => {
    tokenWindow()
    winToken.show()
});

ipcMain.handle('documentsScan', (event) => {
    storage.get('usuario', function(error, user) {
        if (error) throw error;
        console.log('documentsScan')
        console.log(user)


        const data = JSON.stringify({ usuario: user.usuario })
        console.log(data);
        const options = {
            hostname: hostFirma,
            port: portFirma,
            path: urlPathFirma.concat('generarScan'),
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            }
        }

        const req = http.request(options, res => {

            res.on('data', d => {
                let obj = JSON.parse(d);
                console.log(obj);
                if (!isEmpty(obj)) {
                    new Notification({
                        title: "DocumentsScan",
                        body: 'Puede continuar a escanear sus documentos',
                        icon: path.join(__dirname, '/resources/images/origami.png'),
                    }).show()

                } else {
                    new Notification({
                        title: "Escanear documentos",
                        body: 'Intente nuevamente',
                        icon: path.join(__dirname, '/resources/images/origami.png'),
                    }).show()
                }

            })
        })
        req.write(data)
        req.end()
    });
});


ipcMain.handle('get', (event, obj) => {
    getDocumentosFirma(obj)
});

ipcMain.handle('getUsuario', (event, obj) => {
    storage.get('usuario', function(error, data) {
        if (error) throw error;
        console.log('getUsuario')
        console.log(data)
        if (obj === 'FIRMA_ARCHIVO') {
            winP12.webContents.send('usuario', data);
        }
        if (obj === 'INICIO') {
            winhome.webContents.send('usuario', data.usuario);
        }
        if (obj === 'TOKEN') {
            console.log(data.usuario)
            winToken.webContents.send('usuario', data);
        }
    });
});

ipcMain.handle('firmarDoc', (event, obj) => {
    firmarDoc(obj)
});

ipcMain.handle('getTokens', (event, ) => {
    getTokens()
});

ipcMain.handle('firmarDocumento', (event, datosFirma, posicionFirma) => {
    firmarDocumento(datosFirma, posicionFirma)
});

ipcMain.handle('generarImagenDocumento', (event, obj, tipo) => {
    generarImagenDocumento(obj, tipo)
});

ipcMain.handle('abrirDlgExplorador', (event, obj, tipo) => {
    dialog.showOpenDialog(winToken, {
        properties: ['openFile', 'multiSelections'],
        filters: [
            { name: 'Archivos PDF', extensions: ['pdf'], }
        ],
        title: "Escoja su archivo porfi",
        buttonLabel: "Seleccionar",
    }).then(result => {
        console.log(result.canceled)
        console.log(result.filePaths[0])
        if (obj === 'FIRMA_ARCHIVO') {
            winP12.webContents.send('archivoSeleccionado', result.filePaths[0]);
        }
        if (obj === 'TOKEN') {
            winToken.webContents.send('archivoSeleccionado', result.filePaths[0]);
        }
    }).catch(err => {
        console.log(err)
    })

});



function validatelogin(obj) {
    var { user, clave } = obj
    clave = crypto.createHash('md5').update(clave).digest('hex');

    const data = JSON.stringify({ username: user, password: clave })
    console.log(data);
    const options = {
        hostname: hostApp,
        port: portApp,
        path: '/authenticateDesk',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': data.length
        }
    }

    const req = http.request(options, res => {

        res.on('data', d => {
            let obj = JSON.parse(d);
            console.log(obj);
            if (!isEmpty(obj) && !isEmpty(obj.token)) {
                if (obj.token.length > 0) {
                    storage.set('usuario', { id: obj.id, usuario: obj.usuario, nombres: obj.nombres }, function(error) {
                        if (error) throw error;
                    });
                    homeWindow()
                    winhome.show()
                    winlogin.close()
                } else {
                    new Notification({
                        title: "Inicio de sesión",
                        body: 'Usuario o contraseña equivocado',
                        icon: path.join(__dirname, '/resources/images/origami.png'),
                    }).show()
                }

            } else {
                new Notification({
                    title: "Inicio de sesión",
                    body: 'Usuario o contraseña equivocado',
                    icon: path.join(__dirname, '/resources/images/origami.png'),
                }).show()
            }

        })
    })
    req.write(data)
    req.end()
}

function getDocumentosFirma(obj) {
    /*  var data = storage.getSync('usuario');
      url = urlPathApp.concat('origami/docs/documentosPendientes/', data.usuario)
      console.log(url)
      const options = {
          hostname: hostApp,
          port: portApp,
          path: url,
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
          }
      }

      const req = http.request(options, res => {

          res.on('data', d => {
              process.stdout.write(d)
              let results = JSON.parse(d)
              console.log(results)
              if (obj === 'FIRMA_ARCHIVO') {
                  winP12.webContents.send('documents', results)
              }
              if (obj === 'TOKEN') {
                  winToken.webContents.send('documents', results)
              }
          })
      })

      req.end()*/
}

function firmarDoc(obj) {
    winP12.setProgressBar(2)
    const data = JSON.stringify(obj)
    console.log(data);
    const options = {
        hostname: 'localhost',
        port: 8771,
        path: '/api/firmaElectronica/generarDocumentoLocal',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Content-Length': data.length
        }
    }

    const req = http.request(options, res => {

        res.on('data', d => {
            let rest = JSON.parse(d);
            console.log(rest)
            new Notification({
                title: "Documento",
                body: rest.motivo.concat(': ', rest.estado),
                icon: path.join(__dirname, '/resources/images/origami.png'),
            }).show()

        })
        winP12.setProgressBar(0)
        getDocumentosFirma()

    })

    req.on('error', error => {
        console.error(error)
        new Notification({
            title: "FirmaEC",
            body: 'No existe conectividad con firmaEC',
            icon: path.join(__dirname, '/resources/images/origami.png'),
        }).show()
        winP12.setProgressBar(0)
        getDocumentosFirma()
    })
    req.write(data)
    req.end()
}

//OBTIENE TODOS LOS TOKENS DE LA PC

function getTokens() {
    winToken.setProgressBar(2)
    url = urlPathFirma.concat('consultarTokens')
    console.log(url)
    const options = {
        hostname: hostFirma,
        port: portFirma,
        path: url,
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    }

    const req = http.request(options, res => {
        console.log(res)
        res.on('data', d => {
            process.stdout.write(d)
            let results = JSON.parse(d)
            console.log(results)
            winToken.webContents.send('tokens', results)
            winToken.setProgressBar(0)
        })
    })

    req.end()
}





function generarImagenDocumento(obj, type) {
    var { archivo, token, clave, pagina, usuario, usuarioNombre, usuarioId, ubicacion, tipo } = obj

    if (isEmpty(archivo) === true) {
        console.log('isEmpty');
        new Notification({
            title: "Firma de archivo",
            body: 'Ingrese un archivo valido'
        }).show()
    } else if (type === 'TOKEN' && isEmpty(token) || token === 'SELECCIONE') {
        new Notification({
            title: "Escoja un token",
            body: 'Debe ser un token valido'
        }).show()
    } else if (type === 'ARCHIVO' && isEmpty(clave)) {
        new Notification({
            title: "Verifique su firma electrónica",
            body: 'Escriba la clave de su firma electrónica'
        }).show()
    } else if (isEmpty(pagina) === true) {
        new Notification({
            title: "Ingrese el número de la página a firmar",
            body: ''
        }).show()
    } else {

        /**
         * obj -> datos de la firma como el usario el documento
         * res -> el documento en imagen ya covertido
         */

        const data = JSON.stringify({ archivo: archivo, pagina: pagina })
        console.log(data);
        const options = {
            hostname: hostFirma,
            port: portFirma,
            path: urlPathFirma.concat('generarDoc/generar'),
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            }
        }

        const req = http.request(options, res => {

            res.on('data', d => {
                let res = JSON.parse(d);
                console.log(res);
                if (!isEmpty(res)) {
                    estampadoWindow();
                    winEstampado.show();
                    winEstampado.webContents.send('firmaDocumento', res, obj);
                } else {
                    new Notification({
                        title: "Intente nuevamente",
                        body: 'Ocurrio un error al generar el documento, verifique el número de páginas'
                    }).show()
                }

            })
        })
        req.on('error', error => {
            console.error(error)
            new Notification({
                title: "Intente nuevamente",
                body: 'Ocurrio un error al generar el documento, verifique el número de páginas'
            }).show()
        })
        req.write(data)
        req.end()
    }
}

function firmarDocumento(datosFirma, posicionFirma) {

    var { archivo, token, clave, pagina, usuario, usuarioNombre, usuarioId, ubicacion, tipo } = datosFirma
    var { posicionX, posicionY } = posicionFirma

    if (isEmpty(posicionX)) {
        new Notification({
            title: "FirmaEC",
            body: 'Debe escoger correctamente las coordenadas'
        }).show()
    } else if (isEmpty(posicionY)) {
        new Notification({
            title: "FirmaEC",
            body: 'Debe escoger correctamente las coordenadas'
        }).show()
    } else {
        const data = JSON.stringify({
            archivo: archivo,
            pagina: pagina,
            token: token,
            clave: clave,
            usuario: usuario,
            usuarioNombre: usuarioNombre,
            usuarioId: usuarioId,
            ubicacion: ubicacion,
            posicionX: posicionX,
            posicionY: posicionY,
            tipo: tipo
        })
        console.log(data);
        const options = {
            hostname: hostFirma,
            port: portFirma,
            path: urlPathFirma.concat('/firmarDesk'),
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length
            }
        }

        const req = http.request(options, res => {

            res.on('data', d => {
                console.log(d);
                let rest = JSON.parse(d);
                console.log(rest);
                new Notification({
                    title: "Documento Firmado",
                    body: '',
                    icon: path.join(__dirname, '/resources/images/origami.png'),
                }).show()

            })
            getDocumentosFirma()

        })

        req.on('error', error => {
            console.error(error)
            new Notification({
                title: "FirmaEC",
                body: 'No existe conectividad con firmaEC'
            }).show()
            getDocumentosFirma()
        })
        req.write(data)
        req.end()
    }


}



function isEmpty(value) {
    return (value == null || value.length === 0);
}

function run_script(command, args, callback) {
    var child = child_process.spawn(command, args, {
        encoding: 'utf8',
        shell: true
    });
    // You can also use a variable to save the output for when the script closes later
    child.on('error', (error) => {
        dialog.showMessageBox({
            title: 'Title',
            type: 'warning',
            message: 'Error occured.\r\n' + error
        });
    });

    child.stdout.setEncoding('utf8');
    child.stdout.on('data', (data) => {
        //Here is the output
        data = data.toString();
        console.log(data);
    });

    child.stderr.setEncoding('utf8');
    child.stderr.on('data', (data) => {
        // Return some data to the renderer process with the mainprocess-response ID
        mainWindow.webContents.send('mainprocess-response', data);
        //Here is the output from the command
        console.log(data);
    });

    child.on('close', (code) => {
        //Here you can get the exit code of the script  
        switch (code) {
            case 0:
                dialog.showMessageBox({
                    title: 'Title',
                    type: 'info',
                    message: 'End process.\r\n'
                });
                break;
        }

    });
    if (typeof callback === 'function')
        callback();
}