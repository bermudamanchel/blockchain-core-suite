use sha2::{Sha256, Digest};
use std::collections::HashMap;

pub struct ChainValidator {
    difficulty: u32,
    block_cache: HashMap<String, Block>,
}

pub struct Block {
    pub hash: String,
    pub prev_hash: String,
    pub height: u64,
    pub timestamp: u64,
    pub data: String,
}

impl ChainValidator {
    pub fn new(difficulty: u32) -> Self {
        ChainValidator {
            difficulty,
            block_cache: HashMap::new(),
        }
    }

    pub fn validate_block_hash(&self, hash: &str) -> bool {
        let prefix = "0".repeat(self.difficulty as usize);
        hash.starts_with(&prefix)
    }

    pub fn validate_prev_hash(&self, block: &Block) -> bool {
        if block.height == 0 {
            return block.prev_hash == "0000000000000000000000000000000000000000000000000000000000000000";
        }
        self.block_cache.contains_key(&block.prev_hash)
    }

    pub fn validate_timestamp(&self, timestamp: u64) -> bool {
        let now = chrono::Utc::now().timestamp() as u64;
        timestamp > 0 && timestamp <= now
    }

    pub fn full_validate(&mut self, block: Block) -> bool {
        let valid = self.validate_block_hash(&block.hash)
            && self.validate_prev_hash(&block)
            && self.validate_timestamp(block.timestamp)
            && !block.data.is_empty();

        if valid {
            self.block_cache.insert(block.hash.clone(), block);
        }
        valid
    }
}
