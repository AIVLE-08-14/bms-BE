#!/bin/bash
set -e
rm -rf /home/ec2-user/app
mkdir -p /home/ec2-user/app
chown -R ec2-user:ec2-user /home/ec2-user/app