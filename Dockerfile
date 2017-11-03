# Dockerfile to build glpk container images
# Based on Ubuntu

# Set the base image to Ubuntu
FROM picoded/ubuntu-openjdk-8-jdk
# Switch to root for install
USER root
# Install wget
RUN apt-get update -y && apt-get install -y \
	wget \
	build-essential \
	--no-install-recommends \
    libglpk-java \
	&& rm -rf /var/lib/apt/lists/*
# Install glpk from http
# instructions and documentation for glpk: http://www.gnu.org/software/glpk/
WORKDIR /user/local/
RUN wget http://ftp.gnu.org/gnu/glpk/glpk-4.57.tar.gz \
	&& tar -zxvf glpk-4.57.tar.gz

## Verify package contents
# RUN wget http://ftp.gnu.org/gnu/glpk/glpk-4.57.tar.gz.sig \
#	&& gpg --verify glpk-4.57.tar.gz.sig
#	#&& gpg --keyserver keys.gnupg.net --recv-keys 5981E818

WORKDIR /user/local/glpk-4.57
RUN ./configure \
	&& make \
	&& make check \
	&& make install \
	&& make distclean \
	&& ldconfig \
# Cleanup
	&& rm -rf /user/local/glpk-4.57.tar.gz \
	&& apt-get clean
# switch back to user

VOLUME /app
WORKDIR /app