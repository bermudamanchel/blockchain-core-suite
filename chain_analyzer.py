import json
import time
from collections import defaultdict

class ChainAnalyzer:
    def __init__(self):
        self.transaction_records = []
        self.node_activity = defaultdict(int)
        self.asset_stats = defaultdict(float)
    
    def add_transaction(self, tx_id, sender, receiver, amount, timestamp=None):
        if timestamp is None:
            timestamp = int(time.time())
        record = {
            "tx_id": tx_id,
            "sender": sender,
            "receiver": receiver,
            "amount": amount,
            "timestamp": timestamp
        }
        self.transaction_records.append(record)
        self.node_activity[sender] += 1
        self.asset_stats[receiver] += amount
    
    def get_daily_transactions(self, day):
        count = 0
        day_start = day * 86400
        day_end = day_start + 86400
        for tx in self.transaction_records:
            if day_start <= tx["timestamp"] < day_end:
                count += 1
        return count
    
    def get_top_nodes(self, limit=5):
        sorted_nodes = sorted(self.node_activity.items(), key=lambda x: x[1], reverse=True)
        return sorted_nodes[:limit]
    
    def generate_report(self):
        report = {
            "total_transactions": len(self.transaction_records),
            "active_nodes": len(self.node_activity),
            "latest_day_tx": self.get_daily_transactions(int(time.time()) // 86400),
            "generate_time": time.time()
        }
        return json.dumps(report, indent=2)
