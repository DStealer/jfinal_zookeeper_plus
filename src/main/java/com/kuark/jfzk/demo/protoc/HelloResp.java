/**
 * Autogenerated by Avro
 * <p>
 * DO NOT EDIT DIRECTLY
 */
package com.kuark.jfzk.demo.protoc;

import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.SchemaStore;
import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
/** 我也得寒暄几句不是 */
@org.apache.avro.specific.AvroGenerated
public class HelloResp extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
    private static final long serialVersionUID = 4803866098422591868L;
    public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"HelloResp\",\"namespace\":\"com.kuark.jfzk.demo.protoc\",\"doc\":\"我也得寒暄几句不是\",\"fields\":[{\"name\":\"me\",\"type\":{\"type\":\"string\",\"avro.java.string\":\"String\"},\"doc\":\"子账户列表\"}]}");

    public static org.apache.avro.Schema getClassSchema() {
        return SCHEMA$;
    }

    private static SpecificData MODEL$ = new SpecificData();

    private static final BinaryMessageEncoder<HelloResp> ENCODER =
            new BinaryMessageEncoder<HelloResp>(MODEL$, SCHEMA$);

    private static final BinaryMessageDecoder<HelloResp> DECODER =
            new BinaryMessageDecoder<HelloResp>(MODEL$, SCHEMA$);

    /**
     * Return the BinaryMessageDecoder instance used by this class.
     */
    public static BinaryMessageDecoder<HelloResp> getDecoder() {
        return DECODER;
    }

    /**
     * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
     * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
     */
    public static BinaryMessageDecoder<HelloResp> createDecoder(SchemaStore resolver) {
        return new BinaryMessageDecoder<HelloResp>(MODEL$, SCHEMA$, resolver);
    }

    /** Serializes this HelloResp to a ByteBuffer. */
    public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
        return ENCODER.encode(this);
    }

    /** Deserializes a HelloResp from a ByteBuffer. */
    public static HelloResp fromByteBuffer(
            java.nio.ByteBuffer b) throws java.io.IOException {
        return DECODER.decode(b);
    }

    /** 子账户列表 */
    @Deprecated
    public java.lang.String me;

    /**
     * Default constructor.  Note that this does not initialize fields
     * to their default values from the schema.  If that is desired then
     * one should use <code>newBuilder()</code>.
     */
    public HelloResp() {
    }

    /**
     * All-args constructor.
     * @param me 子账户列表
     */
    public HelloResp(java.lang.String me) {
        this.me = me;
    }

    public org.apache.avro.Schema getSchema() {
        return SCHEMA$;
    }

    // Used by DatumWriter.  Applications should not JZCli.
    public java.lang.Object get(int field$) {
        switch (field$) {
            case 0:
                return me;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    // Used by DatumReader.  Applications should not JZCli.
    @SuppressWarnings(value = "unchecked")
    public void put(int field$, java.lang.Object value$) {
        switch (field$) {
            case 0:
                me = (java.lang.String) value$;
                break;
            default:
                throw new org.apache.avro.AvroRuntimeException("Bad index");
        }
    }

    /**
     * Gets the value of the 'me' field.
     * @return 子账户列表
     */
    public java.lang.String getMe() {
        return me;
    }

    /**
     * Sets the value of the 'me' field.
     * 子账户列表
     * @param value the value to set.
     */
    public void setMe(java.lang.String value) {
        this.me = value;
    }

    /**
     * Creates a new HelloResp RecordBuilder.
     * @return A new HelloResp RecordBuilder
     */
    public static com.kuark.jfzk.demo.protoc.HelloResp.Builder newBuilder() {
        return new com.kuark.jfzk.demo.protoc.HelloResp.Builder();
    }

    /**
     * Creates a new HelloResp RecordBuilder by copying an existing Builder.
     * @param other The existing builder to copy.
     * @return A new HelloResp RecordBuilder
     */
    public static com.kuark.jfzk.demo.protoc.HelloResp.Builder newBuilder(com.kuark.jfzk.demo.protoc.HelloResp.Builder other) {
        return new com.kuark.jfzk.demo.protoc.HelloResp.Builder(other);
    }

    /**
     * Creates a new HelloResp RecordBuilder by copying an existing HelloResp instance.
     * @param other The existing instance to copy.
     * @return A new HelloResp RecordBuilder
     */
    public static com.kuark.jfzk.demo.protoc.HelloResp.Builder newBuilder(com.kuark.jfzk.demo.protoc.HelloResp other) {
        return new com.kuark.jfzk.demo.protoc.HelloResp.Builder(other);
    }

    /**
     * RecordBuilder for HelloResp instances.
     */
    public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<HelloResp>
            implements org.apache.avro.data.RecordBuilder<HelloResp> {

        /** 子账户列表 */
        private java.lang.String me;

        /** Creates a new Builder */
        private Builder() {
            super(SCHEMA$);
        }

        /**
         * Creates a Builder by copying an existing Builder.
         * @param other The existing Builder to copy.
         */
        private Builder(com.kuark.jfzk.demo.protoc.HelloResp.Builder other) {
            super(other);
            if (isValidValue(fields()[0], other.me)) {
                this.me = data().deepCopy(fields()[0].schema(), other.me);
                fieldSetFlags()[0] = true;
            }
        }

        /**
         * Creates a Builder by copying an existing HelloResp instance
         * @param other The existing instance to copy.
         */
        private Builder(com.kuark.jfzk.demo.protoc.HelloResp other) {
            super(SCHEMA$);
            if (isValidValue(fields()[0], other.me)) {
                this.me = data().deepCopy(fields()[0].schema(), other.me);
                fieldSetFlags()[0] = true;
            }
        }

        /**
         * Gets the value of the 'me' field.
         * 子账户列表
         * @return The value.
         */
        public java.lang.String getMe() {
            return me;
        }

        /**
         * Sets the value of the 'me' field.
         * 子账户列表
         * @param value The value of 'me'.
         * @return This builder.
         */
        public com.kuark.jfzk.demo.protoc.HelloResp.Builder setMe(java.lang.String value) {
            validate(fields()[0], value);
            this.me = value;
            fieldSetFlags()[0] = true;
            return this;
        }

        /**
         * Checks whether the 'me' field has been set.
         * 子账户列表
         * @return True if the 'me' field has been set, false otherwise.
         */
        public boolean hasMe() {
            return fieldSetFlags()[0];
        }


        /**
         * Clears the value of the 'me' field.
         * 子账户列表
         * @return This builder.
         */
        public com.kuark.jfzk.demo.protoc.HelloResp.Builder clearMe() {
            me = null;
            fieldSetFlags()[0] = false;
            return this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public HelloResp build() {
            try {
                HelloResp record = new HelloResp();
                record.me = fieldSetFlags()[0] ? this.me : (java.lang.String) defaultValue(fields()[0]);
                return record;
            } catch (java.lang.Exception e) {
                throw new org.apache.avro.AvroRuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static final org.apache.avro.io.DatumWriter<HelloResp>
            WRITER$ = (org.apache.avro.io.DatumWriter<HelloResp>) MODEL$.createDatumWriter(SCHEMA$);

    @Override
    public void writeExternal(java.io.ObjectOutput out)
            throws java.io.IOException {
        WRITER$.write(this, SpecificData.getEncoder(out));
    }

    @SuppressWarnings("unchecked")
    private static final org.apache.avro.io.DatumReader<HelloResp>
            READER$ = (org.apache.avro.io.DatumReader<HelloResp>) MODEL$.createDatumReader(SCHEMA$);

    @Override
    public void readExternal(java.io.ObjectInput in)
            throws java.io.IOException {
        READER$.read(this, SpecificData.getDecoder(in));
    }

}
