class RoomTable extends React.Component {
    constructor(props) {
        super(props);

        const {URL, ROBBY_WEBSOCKET_URL}= new Constants();
        this.URL = URL;
        this.ROBBY_WEBSOCKET_URL = ROBBY_WEBSOCKET_URL;

        this.state = {
            roomInfo: [],
            selectedRoomId: 0,
            selectedRoomName: ""
        };

        // this.makeTable = this.makeTable.bind(this);
        this.clickRoom = this.clickRoom.bind(this);
    }

    getRooms() {
        console.log("getRooms");
        $.get([
            this.URL + "/robby/rooms",
        ]).done(function(data) {
            this.setState({roomInfo: data});
        }.bind(this));
    };

    clickRoom(event) {

        console.log("clickRoom");
        console.log(event.target.parentNode.getAttribute('id'));

        this.setState({
            selectedRoomId: event.target.getAttribute('id'),
            selectedRoomName: event.target.getAttribute('name')
        });

        window.location.href = this.URL + "/room/" + event.target.parentNode.getAttribute('id');

    }

    componentDidMount() {
        // console.log(this.state.roomInfo);
    };


    componentWillMount() {
        this.getRooms();
        // console.log(this.state.roomInfo);
    };

    makeTableHeader() {
        let keys = this.state.roomInfo[0];

        let header = [];
        for ( let k in keys ) {
            // TODO: need key to optimize arr rendering performance
            header.push(<td>{k}</td>);
        }
        console.log(header);

        return (<tr>{header}</tr>);
    }

    makeTable(roomInfos) {
        // console.log(roomInfos);

        let rows = [];
        for ( let roomInfo of roomInfos ) {

            let row = [];
            for ( let k in roomInfo ) {
                // TODO: need key to optimize arr rendering performance
                row.push(<td>{roomInfo[k]}</td>);
            }
            // rows.push(<tr id={roomInfo.id} name={roomInfo.name} onClick={this.clickRoom}>{row}</tr>);
            rows.push(<tr id={roomInfo.id} name={roomInfo.name} onClick={this.props.enterRoom}>{row}</tr>);
        }
        return (rows);
    };

    makeTab() {
        let tabs = [];
        tabs.push(<li className="nav-item">
            <a className="nav-link active" id="home-tab" data-toggle="tab" href="#home" role="tab" aria-controls="home" aria-selected="true">Home</a>
        </li>);
        tabs.push(<li className="nav-item">
            <a className="nav-link" id="profile-tab" data-toggle="tab" href="#profile" role="tab" aria-controls="profile" aria-selected="false">Profile</a>
        </li>);
        tabs.push(<li className="nav-item">
            <a className="nav-link" id="contact-tab" data-toggle="tab" href="#contact" role="tab" aria-controls="contact" aria-selected="false">Contact</a>
        </li>);

        return tabs;
    }

    // makeTable = (roomInfos) => {
    //     console.log(roomInfos);
    // };

    render() {
        // console.log(this.state.roomInfo);
        const tabs = this.makeTab();
        const header = this.makeTableHeader();
        const body = this.makeTable(this.state.roomInfo);

        return (
            <div>
                <ul className="nav nav-tabs" id="myTab" role="tablist">
                    {tabs}
                </ul>

                <div className="tab-content" id="myTabContent">
                    <div className="tab-pane fade show active" id="home" role="tabpanel" aria-labelledby="home-tab">
                        <table className="table">
                            <thead>
                                {header}
                            </thead>
                            <tbody>
                                {body}
                            </tbody>
                        </table>
                    </div>
                    <div className="tab-pane fade" id="profile" role="tabpanel" aria-labelledby="profile-tab">
                        <table className="table">
                            <thead>
                            {header}
                            </thead>
                            <tbody>
                            {body}
                            </tbody>
                        </table>
                    </div>
                    <div className="tab-pane fade" id="contact" role="tabpanel" aria-labelledby="contact-tab">
                        <table className="table">
                            <thead>
                            {header}
                            </thead>
                            <tbody>
                            {body}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        );
    }
}
