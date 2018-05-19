class App extends React.Component {
    render() {
        return (
            <div>
                <TopMenu />
                <Robby />
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));