
FROM ubuntu:latest

ENV DEBIAN_FRONTEND=noninteractive 
RUN apt-get update && \
    apt-get install -y build-essential libstdc++-10-dev zlib1g-dev curl wget libssl-dev pkg-config cmake python3 python3-pip

RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs | sh -s -- -y

ADD . /main

WORKDIR /main

RUN /root/.cargo/bin/cargo build --release &&  \
    mv target/release/dryadsynth /usr/bin/dryadsynth && \
    mv target/release/dryadsynth-bv /usr/bin/dryadsynth-bv && \
    mv target/release/synthphonia /usr/bin/synthphonia

FROM busybox


COPY --from=0 /lib64/ld-linux-x86-64.so.2 /lib64/ld-linux-x86-64.so.2 
COPY --from=0 /lib/x86_64-linux-gnu/libpthread.so.0 /lib/x86_64-linux-gnu/libpthread.so.0
COPY --from=0 /lib/x86_64-linux-gnu/libstdc++.so.6 /lib/x86_64-linux-gnu/libstdc++.so.6
COPY --from=0 /lib/x86_64-linux-gnu/libgomp.so.1 /lib/x86_64-linux-gnu/libgomp.so.1
COPY --from=0 /lib/x86_64-linux-gnu/libdl.so.2 /lib/x86_64-linux-gnu/libdl.so.2
COPY --from=0 /lib/x86_64-linux-gnu/libc.so.6 /lib/x86_64-linux-gnu/libc.so.6
COPY --from=0 /lib/x86_64-linux-gnu/libgcc_s.so.1 /lib/x86_64-linux-gnu/libgcc_s.so.1
COPY --from=0 /lib/x86_64-linux-gnu/libm.so.6 /lib/x86_64-linux-gnu/libm.so.6

COPY --from=0 /usr/bin/dryadsynth /usr/bin/dryadsynth
COPY --from=0 /usr/bin/dryadsynth-bv /usr/bin/dryadsynth-bv
COPY --from=0 /usr/bin/synthphonia /usr/bin/synthphonia

CMD ["dryadsynth"]


