
class RobbyContainer extends React.Component {

    constructor(props) {
        super(props);

        this.constants = new Constants();

        this.OPEN = "!OPEN";
        this.MAKE = "!MAKE";
        this.UPDATE = "!UPDATE";
        this.DELETE = "!DELETE";
        this.CLOSE = "!CLOSE";

        this.state = {
            url: this.constants.URL,
            roomInfo: null,
            roomName: "",
            selectedRoomId: 0,
            selectedRoomName: "",
            connection: new WebSocket(this.constants.ROBBY_WEBSOCKET_URL)

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
        this.updateRoom = this.updateRoom.bind(this);
        this.deleteRoom = this.deleteRoom.bind(this);

        this.handleWindowClose = this.handleWindowClose.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
        this.componentWillUnmount = this.componentWillUnmount.bind(this);
    }

    handleWindowClose() {
        this.state.connection.send(this.CLOSE);
    }

    componentDidMount() {
        console.log("didMount");
        this.getRooms();
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

    /*
    room을 만들 시 생성 후 바로 해당 room으로 이동
    */
    makeRoom(event) {
        console.log("makeRoom");
        console.log("roomName : ", this.state.roomName);

        $.post(this.state.url + "/robby",
            {
                roomName: this.state.roomName
            }).done(function(data) {

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
                        <div className={"item"} key={room.id}>
                            <div id={"room_id"}>
                                {room.id}
                            </div>

                            <div className={"content"}>
                                <text className={"header"}>{room.name}</text>
                                <div id={room.id} className={"ui item"} name={room.name} onClick={this.joinRoom}>room.id : {room.id}</div>
                                <div id={room.id} className={"ui item"} name={room.name} onClick={this.clickRoom}>room.name : {room.name}</div>
                            </div>
                        </div>

                    )
                )
            )

        } else {
            return <h1>"loading"</h1>;
        }

    }
    

    render() {

        var roomsLayout = {
            margin: 0
        };

        // return (
        //     <form>
        //
        //         <div id={"user_info"} className={"ui fixed menu"}>
        //             <div className={"ui container"}>
        //                 <a href={"#"} className={"header item"}>
        //                     user_info
        //                 </a>
        //                 <a href={"#"} className={"item"}>
        //                     item
        //                 </a>
        //                 <div className={"ui simple dropdown item"}>
        //                     dropdown
        //                     <i className={"dropdown icon"}></i>
        //                     <div className={"menu"}>
        //                         <a className={"item"} href="#">Link Item</a>
        //                         <a className={"item"} href="#">Link Item</a>
        //                         <div className={"divider"}></div>
        //                         <div className={"header"}>Header Item</div>
        //                         <div className={"item"}>
        //                             <i className={"dropdown icon"}></i>
        //                             Sub Menu
        //                             <div className={"menu"}>
        //                                 <a className={"item"} href="#">Link Item</a>
        //                                 <a className={"item"} href="#">Link Item</a>
        //                             </div>
        //                         </div>
        //                         <a className={"item"} href="#">Link Item</a>
        //                     </div>
        //                 </div>
        //             </div>
        //         </div>
        //
        //         {/*Responsive item?  https://semantic-ui.com/examples/responsive.html  */}
        //         {/*<div id={"rooms"} className={"ui content list"}>*/}
        //         {/*empty slot for layout*/}
        //         <div className={"ui menu"} style={roomsLayout}>
        //         </div>
        //         <div className={"ui relaxed fixed"}>
        //             메뉴
        //         </div>
        //         <div id={"rooms"} className={"ui relaxed divided items"}>
        //
        //             {this.showRooms()}
        //
        //         </div>
        //
        //         <div id={"room_interaction"} className={"ui footer list"}>
        //             <div id={'create'} className={"ui item"}>
        //                 <label>
        //                     Room name :
        //                     <input type="text" value={this.state.roomName} onChange={this.handleRoomNameChange}/>
        //                 </label>
        //                 <button className="ui button" type={'button'} onClick={event => this.makeRoom(event)}>create</button>
        //             </div>
        //
        //             <div id={'update'} className={"ui item"}>
        //                 <label>
        //                     selected Room name :
        //                     <input type="text" value={this.state.selectedRoomName} onChange={this.handleSelectedRoomNameChange}/>
        //                 </label>
        //                 <button className="ui button" type={'button'} onClick={this.updateRoom}>update</button>
        //             </div>
        //
        //             <div id={'delete'} className={"ui item"}>
        //                 <label>
        //                     Room name for delete :
        //                     <input type="text" value={this.state.selectedRoomName} onChange={this.handleSelectedRoomNameChange}/>
        //                 </label>
        //                 <button className="ui button" type={'button'} onClick={this.deleteRoom}>delete</button>
        //             </div>
        //         </div>
        //
        //     </form>
        //
        // );

        // const robbyClass = "jumbotron d-flex align-items-center";
        const robbyClass = "container";

        return (
            <div className={robbyClass}>
                <NotificationBox />
                <RoomTable />
                <RoomInPanel />
            </div>
        );

    }

}


ReactDOM.render(
    <RobbyContainer />,
    document.getElementById('root')
);