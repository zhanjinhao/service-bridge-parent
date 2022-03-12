package sb.core.server.operation;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import sb.core.operation.write.BasicWriteOperation;
import sb.core.operation.write.BasicWriteOperationInterface;
import sb.core.server.Result;
import sb.core.server.util.ServerResponseUtil;

public class OperationServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        String uri = trimUri(msg.uri());
        System.out.println(uri);

        if (uri.startsWith("/api/")) {
            uri = uri.replaceFirst("/api/", "");
            if (uri.startsWith("jar/")) {
                handleJarRequest(uri.replaceFirst("jar/", ""), ctx);
            } else if (uri.startsWith("stub/")) {
                handleStubRequest(uri.replaceFirst("stub/", ""), ctx);
            } else if (uri.startsWith("skeleton/")) {
                handleSkeletonRequest(uri.replaceFirst("skeleton/", ""), ctx);
            } else {
                handleErrorRequest(ctx);
            }
        } else {
            handleErrorRequest(ctx);
        }
    }


    private void handleErrorRequest(ChannelHandlerContext ctx) {
        ServerResponseUtil.doTextResponse(ctx, "error uri");
    }

    private void handleJarRequest(String uri, ChannelHandlerContext ctx) {
        BasicWriteOperationInterface basicWriteOperation = new BasicWriteOperation();
        System.out.println(uri);
        Result result = Result.getSuccess();
        try {
            if (uri.startsWith("load/")) {
                uri = uri.replace("load/", "");
                String[] split = uri.split("/");
                basicWriteOperation.loadJar(split[0], split[1]);
            } else if (uri.startsWith("release/")) {
                uri = uri.replace("release/", "");
                String[] split = uri.split("/");
                basicWriteOperation.releaseJar(split[0], split[1], true);
            }
        } catch (Exception e) {
            result = Result.getError();
        }
        ServerResponseUtil.doTextResponse(ctx, result);

    }

    private void handleStubRequest(String uri, ChannelHandlerContext ctx) {
        BasicWriteOperationInterface basicWriteOperation = new BasicWriteOperation();
        System.out.println(uri);
        Result result = Result.getSuccess();
        try {
            if (uri.startsWith("build/")) {
                uri = uri.replace("build/", "");
                String[] split = uri.split("/");
                basicWriteOperation.buildStub(split[0], split[1], split[2], split[3]);
            } else if (uri.startsWith("remove/")) {
                uri = uri.replace("remove/", "");
                String[] split = uri.split("/");
                basicWriteOperation.removeStub(split[0], split[1], split[2], split[3], true);
            }
        } catch (Exception e) {
            result = Result.getError();
        }
        ServerResponseUtil.doTextResponse(ctx, result);
    }

    private void handleSkeletonRequest(String uri, ChannelHandlerContext ctx) {
        BasicWriteOperationInterface basicWriteOperation = new BasicWriteOperation();
        System.out.println(uri);
        Result result = Result.getSuccess();
        try {
            if (uri.startsWith("build/")) {
                uri = uri.replace("build/", "");
                String[] split = uri.split("/");
                basicWriteOperation.buildSkeleton(split[0], split[1], split[2], split[3]);
            } else if (uri.startsWith("remove/")) {
                uri = uri.replace("remove/", "");
                String[] split = uri.split("/");
                basicWriteOperation.removeSkeleton(split[0], split[1], split[2], split[3]);
            }
        } catch (Exception e) {
            result = Result.getError();
        }
        ServerResponseUtil.doTextResponse(ctx, result);
    }


    private String trimUri(String uri) {
        return uri;
    }


}