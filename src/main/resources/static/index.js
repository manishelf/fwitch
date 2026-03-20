let canvas = document.getElementById('main-canvas');
let context2D = canvas.getContext('2d')
let canvasSize = [window.innerWidth*0.75, window.innerHeight];

function resizeCanvas(canvas, contex2D, canvasSize, scaleFactor){
	canvas.width = canvasSize[0];
	canvas.height = canvasSize[1];

	canvas.style.width = canvasSize[0] + 'px';
    canvas.style.height = canvasSize[1] + 'px';

    // Resize canvas and scale future draws.
    canvas.width = Math.ceil(canvas.width * scaleFactor);
    canvas.height = Math.ceil(canvas.height * scaleFactor);
    contex2D.scale(scaleFactor, scaleFactor);	
}

resizeCanvas(canvas, context2D, canvasSize, 1);

window.onresize = (event)=>{
	// 0.75 should not be hard coded here
	canvasSize = [Number((window.innerWidth * 0.75).toFixed()), window.innerHeight];
	resizeCanvas(canvas, context2D, canvasSize, 1);
}


const {Observable, BehaviourSubject} = rxjs;

//https://github.com/manishelf/qtodo-angular-frontend/blob/master/src/app/service/connection/socket/socket.worker.ts
class ReconnectingWs{
  socket = null;
  maxAttempts = 10;
  backoff = 5000;
  currentAttemptCount = 0;
  reconnect = true;  
  reconnectTimer = -1;
  $message = new BehaviourSubject(null);
  $connected = new BehaviourSubject(false)
  url = '';
  handleError = (e)=>{}

  constructor(url){
    this.url = url;
    this.connect();
  }

  connect(){
    this.socket = new WebSocket(this.url);
    this.socket.onopen = (e)=>{this.onopen(e)};
    this.socket.onclose = (e)=>{this.onclose(e)};
    this.socket.onmessage = (e)=>{this.onmessage(e)};
    this.socket.onerror = (e)=>{console.error(e); this.handleError(e)};
  }

  disconnect(){
    this.socket = null;
  }

  async onmessage(e){
    const data = e.data;
    const arrayBuffer = await data.arrayBuffer();
    console.log("Sock got", arryaBuffer);
    $message.next(arrayBuffer) 
  }

  onopen(e){
    this.reconnect = false;
    clearTimeout(this.reconnectTimer);
    this.currentAttemptCount = 0;
    this.reconnectTimer = -1;
    this.$connected.next(true);    
  }

  onclose(e){
    console.log("Disconnected ", e.wasClean, e.reason);
    
    if(!e.wasClean){
      this.reconnect = true;
      clearTimeout(this.reconnectTimer);
      if(this.currentAttemptCount<this.maxAttempts){
        setTimeout(()=>{
          this.connect();
          this.currentAttemptCount++;
        }, Math.pow(2, this.currentAttemptCount)*this.backoff);
      }
	}
  	this.$connected.next(false);    
  }

  send(d){
    if(this.socket?.readyState == WebSocket.OPEN){
      this.socket?.send(d);
      console.log("Socket send ", d);
            
      if(this.messageQueue.length!=0){
        this.messageQueue.forEach((d)=>{
          this.send(d);
        });
        this.messageQueue = [];
      }
    }else{
      this.messageQueue.push(buffer);
    }
  }
}

socketMedia = new ReconnectingWs("wss://localhost:8080/fwitch/media")
socketChat = new ReconnectingWs("wss://localhost:8080/fwitch/chat")

socketMedia.$connected.pipe(tap((status)=>{
	console.log("socket status", status)	
}))

let mediaSubscription = socketMedia.$message.subscribe((message)=>{
	console.log(message)
})

if(mediaSubscription){
	mediaSubscription.unsubscribe()
}