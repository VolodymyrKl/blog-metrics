import React, {useEffect, useState} from 'react';
import SockJS from 'sockjs-client';
import {Client, IMessage} from '@stomp/stompjs';

interface WordCount {
    [key: string]: number;
}

const WordCountDisplayComponent: React.FC = () => {
    const [messages, setMessages] = useState<WordCount>();
    const stompClientRef = React.useRef<Client | null>(null);
    const stompClient: Client = new Client({
        webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
        reconnectDelay: 500,
    });

    useEffect(() => {
        stompClient.onConnect = (frame: any): void => {
            console.log('Connected: ' + frame);

            requestPostUpdates();

            stompClient.subscribe('/api/word-count', (message: IMessage) => {
                const content = JSON.parse(message.body);
                setMessages(content);
            });
        };

        stompClient.onWebSocketError = (error: Error): void => {
            console.error('Error with websocket', error);
        };

        stompClient.onStompError = (frame: any): void => {
            console.error('Broker reported error: ' + frame.headers['message']);
            console.error('Additional details: ' + frame.body);
        };

        stompClient.activate();
        stompClientRef.current = stompClient;

        return () => {
            console.log('Component unmounted, but WebSocket remains active.');
        };
    }, []);

    const requestPostUpdates = () => {
        if (stompClient.connected) {
            stompClient.publish({
                destination: "/app/check-updates",
            });
        }
    };

    return (
        <div>
            <h1>WebSocket Messages</h1>
            <table id="word-count">
                <thead>
                <tr>
                    <th>Message</th>
                </tr>
                </thead>
                <tbody>
                {messages && Object.entries(messages).map(([word, count]) => (
                    <tr key={word}>
                        <td>{word}: {count}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default WordCountDisplayComponent;