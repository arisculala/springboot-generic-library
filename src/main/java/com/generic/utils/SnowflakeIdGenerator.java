package com.generic.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * SnowflakeIdGenerator is a distributed unique ID generator inspired by
 * Twitter's Snowflake.
 * It generates 64-bit unique IDs that are roughly time-ordered and suitable for
 * use in distributed systems.
 * 
 * The ID is composed of:
 * - Timestamp (milliseconds since a custom epoch)
 * - Node ID (unique identifier for the generator instance)
 * - Sequence number (to handle multiple IDs generated in the same millisecond)
 * 
 * The structure of the ID is as follows:
 * | Timestamp (bits) | Node ID (bits) | Sequence (bits) |
 * |------------------|----------------|-----------------|
 * | 41 bits | 10 bits | 12 bits |
 */
public class SnowflakeIdGenerator {
    // Custom epoch (Jan 1, 2023 in milliseconds)
    private static final long EPOCH = 1672531200000L;

    // Number of bits allocated for the node ID
    private static final long NODE_ID_BITS = 10;

    // Number of bits allocated for the sequence number
    private static final long SEQUENCE_BITS = 12;

    // Maximum value for the node ID (2^10 - 1)
    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;

    // Maximum value for the sequence number (2^12 - 1)
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    // Node ID for this generator instance
    private final long nodeId;

    // Timestamp of the last generated ID
    private long lastTimestamp = -1L;

    // Sequence number for IDs generated in the same millisecond
    private final AtomicLong sequence = new AtomicLong(0);

    // Singleton instance of the SnowflakeIdGenerator
    private static volatile SnowflakeIdGenerator instance;

    /**
     * Private constructor to enforce singleton pattern.
     * 
     * @param nodeId Unique identifier for this generator instance (0 to 1023).
     * @throws IllegalArgumentException if nodeId is out of range.
     */
    private SnowflakeIdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + MAX_NODE_ID);
        }
        this.nodeId = nodeId;
    }

    /**
     * Returns the singleton instance of SnowflakeIdGenerator.
     * 
     * @param nodeId Unique identifier for this generator instance (0 to 1023).
     * @return Singleton instance of SnowflakeIdGenerator.
     */
    public static SnowflakeIdGenerator getInstance(long nodeId) {
        if (instance == null) {
            synchronized (SnowflakeIdGenerator.class) {
                if (instance == null) {
                    instance = new SnowflakeIdGenerator(nodeId);
                }
            }
        }
        return instance;
    }

    /**
     * Generates a unique 64-bit ID.
     * 
     * @return A unique ID.
     * @throws IllegalStateException if the system clock moves backwards.
     */
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();

        // Check if the current timestamp is less than the last timestamp (clock moved
        // backwards)
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate ID");
        }

        // If the current timestamp is the same as the last timestamp, increment the
        // sequence number
        if (timestamp == lastTimestamp) {
            long seq = (sequence.incrementAndGet()) & MAX_SEQUENCE;
            if (seq == 0) {
                // If the sequence number overflows, wait until the next millisecond
                while ((timestamp = System.currentTimeMillis()) <= lastTimestamp) {
                    // Busy wait
                }
            }
        } else {
            // If the current timestamp is greater than the last timestamp, reset the
            // sequence number
            sequence.set(0);
        }

        // Update the last timestamp
        lastTimestamp = timestamp;

        // Generate the ID by combining the timestamp, node ID, and sequence number
        return ((timestamp - EPOCH) << (NODE_ID_BITS + SEQUENCE_BITS)) |
                (nodeId << SEQUENCE_BITS) |
                sequence.get();
    }
}