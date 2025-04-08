import 'package:stomp_dart_client/stomp_dart_client.dart';

class WebSocketManager {
  StompClient? stompClient;
  bool _isConnected = false;
  // 연결 상태 변경 콜백
  Function(bool isConnected)? onConnectionStatusChanged;

  // 연결 상태 접근자
  bool get isConnected => _isConnected;

  // 연결 상태 설정 (내부용)
  set _connected(bool value) {
    if (_isConnected != value) {
      _isConnected = value;
      if (onConnectionStatusChanged != null) {
        onConnectionStatusChanged!(_isConnected);
      }
    }
  }

  void connect({
    String? userToken,
    required void Function(StompFrame frame) onConnectCallback,
    void Function(dynamic error)? onError,
  }) {
    // 헤더 설정 (토큰이 있는 경우에만 인증 헤더 추가)
    Map<String, String> connectHeaders = {};
    Map<String, String> stompHeaders = {};

    if (userToken != null && userToken.isNotEmpty) {
      connectHeaders['Authorization'] = 'Bearer $userToken';
      stompHeaders['Authorization'] = 'Bearer $userToken';
    }

    stompClient = StompClient(
        config: StompConfig(
      url: 'wss://j12e206.p.ssafy.io/ws-stomp', // ✅ WebSocket 엔드포인트
      onConnect: (frame) {
        _connected = true;
        onConnectCallback(frame);
      },
      onWebSocketError: (error) {
        _connected = false;
        if (onError != null) {
          onError(error);
        } else {
          print('❌ WebSocket Error: $error');
        }
      },
      onDisconnect: (frame) {
        _connected = false;
        print('🔌 WebSocket Disconnected');
      },
      onStompError: (frame) {
        _connected = false;
        print('⚠️ STOMP Protocol Error: ${frame.body}');
      },
      beforeConnect: () async {
        print('🔌 Connecting to WebSocket...');
        await Future.delayed(const Duration(milliseconds: 200));
      },
      stompConnectHeaders: stompHeaders,
      webSocketConnectHeaders: connectHeaders,
      heartbeatIncoming: const Duration(seconds: 5),
      heartbeatOutgoing: const Duration(seconds: 5),
    ));

    stompClient!.activate();
  }

  void disconnect() {
    if (stompClient != null) {
      stompClient?.deactivate();
      _connected = false;
    }
  }

  void subscribeToRoom({
    required int fundingId,
    required int userId,
    required void Function(StompFrame frame) onMessage,
  }) {
    final destination = 'wss://j12e206.p.ssafy.io/sub/chat/$fundingId';

    stompClient?.subscribe(
      destination: destination,
      headers: {
        'userId': userId.toString(),
      },
      callback: onMessage,
    );
  }
}
