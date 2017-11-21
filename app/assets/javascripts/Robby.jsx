
class RobbyContainer extends React.Component {

    constructor(props) {
        super(props);

        this.OPEN = "!OPEN";
        this.MAKE = "!MAKE";
        this.UPDATE = "!UPDATE";
        this.DELETE = "!DELETE";
        this.CLOSE = "!CLOSE";

        this.state = {
            url: 'http://localhost:9000',
            roomInfo: null,
            roomName: "",
            selectedRoomId: 0,
            selectedRoomName: ""
            ,connection: new WebSocket('ws://localhost:9000/robby/websocket')

        };


        this.state.connection.onopen = function () {
            this.state.connection.send(this.OPEN); //
        }.bind(this);


        this.state.connection.onerror = function (error) {
            console.log('WebSocket Error ' + error);
            console.log(error);
        }.bind(this);

        //to receive the message from server
        this.state.connection.onmessage = function (e) {
            console.log('message from server: ' + e.data);
            this.getRooms();
        }.bind(this);

        this.state.connection.onclose = function (e) {

            this.state.connection.send(this.CLOSE);
            console.log("onClose");

        }.bind(this);

        this.handleRoomNameChange = this.handleRoomNameChange.bind(this);
        this.handleSelectedRoomNameChange = this.handleSelectedRoomNameChange.bind(this);
        this.makeRoom = this.makeRoom.bind(this);
        this.showRooms = this.showRooms.bind(this);
        this.clickRoom = this.clickRoom.bind(this);
        this.updateRoom = this.updateRoom.bind(this);
        this.deleteRoom = this.deleteRoom.bind(this);

        this.handleWindowClose = this.handleWindowClose.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.componentWillUnmount = this.componentWillUnmount.bind(this);

        this.getRooms();

    }

    handleWindowClose() {
        this.state.connection.send(this.CLOSE);
    }

    componentDidMount() {
        console.log("didMount");
        window.addEventListener('beforeunload', this.handleWindowClose);
    }

    componentWillUnmount() {
        console.log("unmount");
        window.removeEventListener('beforeunload', this.handleWindowClose);
    }

    handleRoomNameChange(e) {
        this.setState({roomName: e.target.value})
    }
    handleSelectedRoomNameChange(e) {
        this.setState({selectedRoomName: e.target.value})
    }


    getRooms() {

        console.log("getRooms");

        $.get([
            this.state.url + "/robby/rooms",
        ]).done(function(data) {
            this.setState({roomInfo: data});
        }.bind(this));

    };

    makeRoom(event) {
        console.log("makeRoom");
        console.log("roomName : ", this.state.roomName);

        $.post(this.state.url + "/robby",
            {
                roomName: this.state.roomName
            }).done(function(data) {
            // console.log('done post');
            // console.log(data);
            this.getRooms();
            this.state.connection.send(this.MAKE); //
        }.bind(this));

        event.preventDefault();
    }

    updateRoom(event) {
        console.log('updateRoom');
        console.log(event.target);
        console.log(this.state.selectedRoomId);
        console.log(this.state.selectedRoomName);

        $.post(this.state.url + "/robby/" + this.state.selectedRoomId,
            {
                roomName: this.state.selectedRoomName
            }).done(function(data) {
            // console.log('done post');
            // console.log(data);
            this.getRooms();
            this.state.connection.send(this.UPDATE);
        }.bind(this));

        event.preventDefault();
    }


    deleteRoom(event) {
        console.log('deleteRoom');
        console.log(this.state.selectedRoomId);

        $.ajax({
            url: this.state.url + "/robby/" + this.state.selectedRoomId,
            type: "DELETE",
            success: function(data) {
                this.getRooms();
                this.state.connection.send(this.DELETE);
            }.bind(this)
        });

        event.preventDefault();
    }

    showRooms() {

        console.log("showRooms");

        if(this.state.roomInfo !== null) {

            return (
                this.state.roomInfo.map(
                    (room, index) => (
                        <h1 id={room.id} name={room.name} onClick={this.clickRoom} key={room.id}>{room.id}  {room.name}</h1>
                    )
                )
            )

        } else {
            return <h1>"loading"</h1>;
        }

    }

    clickRoom(event) {

        console.log("clickRoom");
        // console.log(event.target);
        // console.log(event.target.getAttribute('id'));
        // console.log(event.target.getAttribute('name'));
        // console.log(this.state.selectedRoomId);
        // console.log(this.state.selectedRoomName);

        this.setState({
            selectedRoomId: event.target.getAttribute('id'),
            selectedRoomName: event.target.getAttribute('name')
        });

        window.location.href = this.state.url + "/room/" + event.target.getAttribute('id');

    }

    render() {

        return (
            <form>

                <di id={'create'}>
                    <label>
                        Room name :
                        <input type="text" value={this.state.roomName} onChange={this.handleRoomNameChange}/>
                    </label>
                    <button className="ui button" type={'button'} onClick={event => this.makeRoom(event)}>create</button>
                </di>

                <di id={'update'}>
                    <label>
                        selected Room name :
                        <input type="text" value={this.state.selectedRoomName} onChange={this.handleSelectedRoomNameChange}/>
                    </label>
                    <button className="ui button" type={'button'} onClick={this.updateRoom}>update</button>
                </di>

                <di id={'delete'}>
                    <label>
                        Room name for delete :
                        <input type="text" value={this.state.selectedRoomName} onChange={this.handleSelectedRoomNameChange}/>
                    </label>
                    <button className="ui button" type={'button'} onClick={this.deleteRoom}>delete</button>
                </di>

                {this.showRooms()}

            </form>


        );

    }

}


ReactDOM.render(
    <RobbyContainer />,
    document.getElementById('root')
);