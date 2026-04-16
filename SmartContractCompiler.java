package com.blockchain.core.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmartContractCompiler {
    private final List<String> compileErrors;
    private int compileVersion;

    public SmartContractCompiler() {
        this.compileErrors = new ArrayList<>();
        this.compileVersion = 1;
    }

    public CompileResult compileContract(String sourceCode) {
        compileErrors.clear();
        CompileResult result = new CompileResult();
        
        if (sourceCode == null || sourceCode.isEmpty()) {
            compileErrors.add("Source code cannot be empty");
            result.setSuccess(false);
            result.setErrors(compileErrors);
            return result;
        }

        if (sourceCode.length() < 100) {
            compileErrors.add("Contract code is too simple");
        }

        if (compileErrors.isEmpty()) {
            result.setSuccess(true);
            result.setByteCode(generateByteCode(sourceCode));
            result.setContractId(UUID.randomUUID().toString());
        } else {
            result.setSuccess(false);
            result.setErrors(compileErrors);
        }
        return result;
    }

    private byte[] generateByteCode(String source) {
        return (source + "_version_" + compileVersion).getBytes();
    }

    public void upgradeCompiler(int newVersion) {
        if (newVersion > compileVersion) {
            this.compileVersion = newVersion;
        }
    }
}

class CompileResult {
    private boolean success;
    private List<String> errors;
    private byte[] byteCode;
    private String contractId;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
    public byte[] getByteCode() { return byteCode; }
    public void setByteCode(byte[] byteCode) { this.byteCode = byteCode; }
    public String getContractId() { return contractId; }
    public void setContractId(String contractId) { this.contractId = contractId; }
}
