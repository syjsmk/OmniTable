class Room extends React.Component {

    constructor(props) {
        super(props);

        this.constants = new Constants();

        this.JOIN = "!JOIN";
        this.EXIT = "!EXIT";

        // 선택한 room의 id는 room.scala.html에서 바인딩된 값을 사용함
        this.state = {
            url: this.constants.URL,
            id: roomId,
            connection: new WebSocket("ws://localhost:9000/room/" + roomId + "/websocket")
        };

        this.state.connection.onopen = function () {
            this.state.connection.send(this.JOIN); //
        }.bind(this);

        this.state.connection.onerror = function (error) {
            console.log('WebSocket Error ' + error);
            console.log(error);
        }.bind(this);

        //to receive the message from server
        this.state.connection.onmessage = function (e) {
            console.log('message from server: ' + e.data);
        }.bind(this);

        this.state.connection.onclose = function (e) {
            this.state.connection.send(this.EXIT);
            console.log("onClose");
        }.bind(this);


        this.getRoom = this.getRoom.bind(this);


        this.handleWindowClose = this.handleWindowClose.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.componentWillUnmount = this.componentWillUnmount.bind(this);

        this.getRoom();
    }

    handleWindowClose() {
        this.state.connection.send(this.EXIT);
    }


    componentDidMount() {
        console.log("didMount");
        window.addEventListener('beforeunload', this.handleWindowClose);
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

        });

    }

    render() {
        return (
            <h1>room</h1>
        )
    }
}

ReactDOM.render(
    <Room/>,
    document.getElementById('root')
);
