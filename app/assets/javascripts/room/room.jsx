class Room extends React.Component {

    constructor(props) {
        super(props);

        this.constants = new Constants();

        this.JOIN = "!JOIN";
        this.EXIT = "!EXIT";
        this.MESSAGE = "!MESSAGE";

        // 선택한 room의 id는 room.scala.html에서 바인딩된 값을 사용함
        this.state = {
            url: this.constants.URL,
            // id: roomId,
            id: this.props.roomId,
            message: '',
            // connection: new WebSocket("ws://localhost:9000/room/" + roomId + "/websocket")
            connection: new WebSocket("ws://localhost:9000/room/" + this.props.roomId + "/websocket")
        };

        this.state.connection.onopen = function () {
            // this.state.connection.send(this.JOIN); //
            this.state.connection.send(JSON.stringify({
                type: this.JOIN
            }));
        }.bind(this);

        this.state.connection.onerror = function (error) {
            console.log('WebSocket Error ' + error);
            console.log(error);
        }.bind(this);

        //to receive the message from server
        this.state.connection.onmessage = function (e) {

            const data = JSON.parse(e.data);

            switch(data.type) {

                case '!JOIN': {
                    console.log(this.JOIN);
                    break;
                }

                case '!MESSAGE': {
                    console.log("receive message from others");
                    this.addMessage(data.message);
                    break;
                }
                default :
                    break;
            }
        }.bind(this);

        this.state.connection.onclose = function (e) {
            this.state.connection.send(this.EXIT);
            console.log("onClose");
        }.bind(this);


        this.getRoom = this.getRoom.bind(this);


        this.handleWindowClose = this.handleWindowClose.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.componentWillUnmount = this.componentWillUnmount.bind(this);
        this.handleMessage = this.handleMessage.bind(this);
        this.sendMessage = this.sendMessage.bind(this);
        this.addMessage = this.addMessage.bind(this);

        this.getRoom();
    }

    handleWindowClose() {
        this.state.connection.send(JSON.stringify({
            type: this.EXIT
        }));
    }


    componentDidMount() {
        console.log("didMount");
        window.addEventListener('beforeunload', this.handleWindowClose);
        window.addEventListener('popstate', function(event) {
            history.back();
        });
    }


    componentWillUnmount() {
        console.log("unmount");
        window.removeEventListener('beforeunload', this.handleWindowClose);
    }


    getRoom() {
        console.log("getRoom");
        console.log(this.state.id);

        $.get(
            this.state.url + "/room/data/" + this.state.id
        ).done(function (data) {
            console.log(data);
        }.bind(this));

    }

    handleMessage(e) {
        this.setState({message: e.target.value})
    }

    sendMessage(e) {
        console.log("sendMessage");
        console.log(this.state.message);

        this.state.connection.send(JSON.stringify({
            type: this.MESSAGE,
            time: new Date().toLocaleString(),
            sender: '',
            message: this.state.message
        }));

    }

    addMessage(message) {
        $("#message_div").append('<p>' + message + '</p>');
    }


    render() {
        return (

            <div>
                <h1>roomId : {this.state.id}</h1>
                <div id={'message_div'}>
                </div>
                <label>
                    message :
                    <input id={'message_input'} type="text" value={this.state.message} onChange={this.handleMessage}/>
                </label>
                <button className="ui button" type={'button'} onClick={event => this.sendMessage(event)}>send</button>
            </div>

        )
    }


}

ReactDOM.render(
    <Room/>,
    document.getElementById('root')
);
