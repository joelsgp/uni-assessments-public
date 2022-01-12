from __future__ import annotations
from typing import Optional

BINARY = '01'

class Encoding:
    def __init__(self, tree_list: list[Optional[str]]):
        self._encoding = tree_list.copy()
        # todo
        self._decoding = {}

        self.decodeText = self.decode_text
        self.encodeText = self.encode_text

    def decode_text(self, coded: str) -> str:
        if any(c not in BINARY for c in coded):
            raise ValueError('The string coded must contain only 1s and 0s.')

        decoded_list = []

        index = 0
        for c in coded:
            node = self._encoding[index]
            if node:
                decoded_list.append(node)
                index = 0

            if c == '0':
                index = 2 * index + 1
            else:
                index = 2 * index + 2

        decoded = ''.join(decoded_list)
        return decoded

    def encode_text(self, text: str) -> str:
        encoded_list = []
        for c in text:
            e = self._decoding.get(c)
            if e is None:
                raise ValueError('Text contains symbols not present in the encoding tree.')
            encoded_list.append(e)

        encoded = ''.join(encoded_list)
        return encoded
